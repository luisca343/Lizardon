package es.allblue.lizardon.client;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.blocks.BloquePantalla;
import es.allblue.lizardon.client.gui.PantallaCine;
import es.allblue.lizardon.client.gui.PantallaSmartRotom;
import es.allblue.lizardon.client.gui.VideoScreen2;
import es.allblue.lizardon.client.renders.IItemRenderer;
import es.allblue.lizardon.client.renders.SmartRotomRenderer;
import es.allblue.lizardon.SharedProxy;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.server.SMessagePadCtrl;
import es.allblue.lizardon.tileentity.PantallaTE;
import es.allblue.lizardon.util.MessageHelper;
import es.allblue.lizardon.util.QueryHelper;
import es.allblue.lizardon.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.example.BrowserScreen;
import net.montoyo.mcef.utilities.Log;
import noppes.npcs.api.event.NpcEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "lizardon", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientProxy extends SharedProxy  implements IDisplayHandler, IJSQueryHandler {
    private API mcef;
    public HashMap<Integer, PadData> padMap;
    public final ArrayList<PadData> padList = new ArrayList<>();
    private SmartRotomRenderer smartRotomRenderer = new SmartRotomRenderer();
    private PantallaSmartRotom backup = null;
    private Minecraft mc = Minecraft.getInstance();
    public static IJSQueryCallback callbackMisiones;
    public static IJSQueryCallback callbackMCEF;
    public String idServidor;

    private final ArrayList<PantallaTE> screenTracking = new ArrayList<>();

    private int lastTracked = 0;
    private int minePadTickCounter = 0;

    public int getNextPadID(){
        return padList.size()+1;
    }

    public class PadData {

        public IBrowser view;
        private boolean isInHotbar;
        private final int id;
        private long lastURLSent;

    private PadData(String url, int id) {
        view = mcef.createBrowser(Lizardon.config.getHome());
        view.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        view.resize((int) 1280, (int)  720);
        isInHotbar = true;
        this.id = id;
    }
}


    public void iniciarPadMap(){
        padMap = new HashMap<>();
    }

    @SubscribeEvent
    public void onRenderPlayerHand(RenderHandEvent ev) {
        Item item = ev.getItemStack().getItem();
        IItemRenderer renderer;

        if(ItemInit.SMARTROTOM.isPresent()) {
            if (item == ItemInit.SMARTROTOM.get()){
                renderer = smartRotomRenderer;
            }
            else
                return;
            renderer.render(ev.getMatrixStack(), ev.getItemStack(), (ev.getHand() == Hand.MAIN_HAND) ? 1.0f : -1.0f, ev.getSwingProgress(), ev.getEquipProgress(), ev.getBuffers(), ev.getLight());
            ev.setCanceled(true);
        }
    }

    @Override
    public void preInit() {
        Lizardon.LOGGER.info("PRE INIT");
        mc = Minecraft.getInstance();
        mcef = Lizardon.getInstance().getAPI();
        MinecraftForge.EVENT_BUS.register(this);


        iniciarPadMap();
        if(mcef != null)
            mcef.registerScheme("lizardon", null, true, false, false, true, true, false, false);
    }

    public void end (FMLLoadCompleteEvent event){
        API api = Lizardon.getInstance().getAPI();
        if(api != null) {
            //Register this class to handle onAddressChange and onQuery events
            //api.registerDisplayHandler(this);
            //prepararNavegador(api);
            Lizardon.LOGGER.info("Registering JSQueryHandler...");
            api.registerJSQueryHandler(this);
        }
    }

    public PadData getPadByID(int id) {
        return padMap.get(id);
    }

    public void displaySetPadURLGui(String padURL) {
        Log.error("Called SharedProxy.displaySetPadURLGui() on es.allblue.lizardon.client side...");
    }

    public void openMinePadGui(int padId) {
        PadData pd = padMap.get(padId);

        if(pd != null && pd.view != null){
            mc.setScreen(new PantallaSmartRotom(pd));
            pd.view.resize(1920, 1080);
        }
    }

    public void closeSmartRotom(){
        mc.setScreen(null);
    }


    @Override
    public void prepararNavegador(API api) {
        IBrowser browser = api.createBrowser("about:blank", false);
        browser.loadURL("about:blank");
    }

    @Override
    public void verVideo(String url) {
        MessageHelper.enviarMensaje(Minecraft.getInstance().player, "Abriendo video " + url);
        mc.setScreen(new VideoScreen2(url, 100));

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent ev) {
        //Load/unload minePads depending on which item is in the player's hand
        if(++minePadTickCounter >= 10) {
            minePadTickCounter = 0;
            PlayerEntity ep = mc.player;

            for(PadData pd: padList)
                pd.isInHotbar = false;

            if(ep != null) {
                updateInventory(ep.inventory.items, ep.getItemInHand(Hand.MAIN_HAND), 9);
                updateInventory(ep.inventory.offhand, ep.getItemInHand(Hand.OFF_HAND), 1); //Is this okay?
            }

            //TODO: Check for GuiContainer.draggedStack

            for(int i = padList.size() - 1; i >= 0; i--) {
                PadData pd = padList.get(i);

                if(!pd.isInHotbar) {
                    pd.view.close();
                    pd.view = null; //This is for GuiMinePad, in case the player dies with the GUI open
                    padList.remove(i);
                    padMap.remove(pd.id);
                }
            }

            if(mc.player != null && !screenTracking.isEmpty()) {
                int id = lastTracked % screenTracking.size();
                lastTracked++;
                PantallaTE tes = screenTracking.get(id);
                Block bloque = mc.level.getBlockState(tes.getBlockPos()).getBlock();
                if(!(bloque instanceof BloquePantalla)){
                    Lizardon.LOGGER.info("LOG: Unloading screen " + id + " at " + tes.getBlockPos() + " because it's not a screen");
                    tes.unload();
                    return;
                }
                double dist2 = mc.player.distanceToSqr(tes.getBlockPos().getX(), tes.getBlockPos().getY(), tes.getBlockPos().getZ());

                MessageHelper.enviarMensaje(mc.player, "Distancia: " + dist2 + " Cargado: " + tes.isLoaded());
                if(tes.isLoaded()) {
                    if(dist2 >  50){
                        Lizardon.LOGGER.info("LOG: Unloading screen " + id + " at " + tes.getBlockPos() + " because it's too far away");
                        tes.unload();
                    }
                    tes.updateTrackDistance(dist2, 80); //ToDo find master volume
                } else if(dist2 <= 50){
                    tes.load();
                    Lizardon.LOGGER.info("LOG: Loading screen " + id + " at " + tes.getBlockPos() + " because it's close enough");
                }
            }


        }
    }

    private void updateInventory(NonNullList<ItemStack> inv, ItemStack heldStack, int cnt) {
        for (int i = 0; i < cnt; i++) {
            ItemStack item = inv.get(i);

            if (ItemInit.SMARTROTOM.isPresent()) {
                if (item.getItem() == ItemInit.SMARTROTOM.get()) {
                    CompoundNBT tag = item.getTag();

                    if (tag != null && tag.contains("PadID"))
                        updatePad(tag.getInt("PadID"), tag, item == heldStack);
                }
            }
        }
    }

    public void updatePad(int id, CompoundNBT tag, boolean isSelected) {
        PadData pd = padMap.get(id);

        if(pd != null)
            pd.isInHotbar = true;
        else if(isSelected && tag.contains("PadURL")) {
            pd = new PadData(tag.getString("PadURL"), id);
            padMap.put(id, pd);
            padList.add(pd);

        }
    }

    public void abrirSmartRotom(){
        mc.setScreen(hasBackup() ? backup : new BrowserScreen());
        backup = null;
    }

    @Override
    public void abrirPantalla(Screen pantalla) {
        Minecraft.getInstance().setScreen(pantalla);
    }

    public boolean hasBackup() {
        return (backup != null);
    }


    @Override
    public void onAddressChange(IBrowser browser, String url) {
        if(browser != null) {
            long t = System.currentTimeMillis();

            for(PadData pd: padList) {
                if(pd.view == browser && t - pd.lastURLSent >= 1000) {
                    if(Lizardon.isSiteBlacklisted(url))
                        pd.view.loadURL(Lizardon.config.getHome());
                    else {
                        pd.lastURLSent = t; //Avoid spamming the server with porn URLs
                        Messages.INSTANCE.sendToServer(new SMessagePadCtrl(pd.id, url));
                    }

                    break;
                }
            }
            /*
            for(TileEntityScreen tes: screenTracking)
                tes.updateClientSideURL(browser, url);
            */
        }


        //Called by MCEF if a browser's URL changes. Forward this event to the screen.
        if(mc.screen instanceof BrowserScreen)
            ((BrowserScreen) mc.screen).onUrlChanged(browser, url);
        else if(hasBackup())
            backup.onUrlChanged(browser, url);
    }

    @Override
    public void onTitleChange(IBrowser iBrowser, String s) {

    }

    @Override
    public void onTooltip(IBrowser iBrowser, String s) {

    }

    @Override
    public void onStatusMessage(IBrowser iBrowser, String s) {

    }

    @Override
    public boolean handleQuery(IBrowser iBrowser, long l, String query, boolean b, IJSQueryCallback callback) {
        return QueryHelper.handleQuery(iBrowser, l , query, b, callback);
    }

    @Override
    public void cancelQuery(IBrowser iBrowser, long l) {
        Lizardon.LOGGER.info("SCRIPT CANCELADO");
    }

    @Override
    public void actualizarNPC(NpcEvent.InteractEvent event) {

    }

    @Override
    public void crearArchivo(String nombre) {
        Path basePath = Minecraft.getInstance().gameDirectory.toPath().toAbsolutePath().toAbsolutePath();
        Path path = Paths.get(basePath.toString(),"Lizardon");
        File carpetaBase = new File(path.toString());
        if(!carpetaBase.exists()) {
            carpetaBase.mkdirs();
        }
    }

    @Override
    public Path getRuta(String ruta) {
        Path basePath = Minecraft.getInstance().gameDirectory.toPath().toAbsolutePath();
        Path path = Paths.get(basePath.toString(),"Lizardon/"+ ruta);
        File fileDatos = new File(path.toString());
        if(!fileDatos.exists()) {
            Lizardon.LOGGER.info("Creando archivo inexistente "+ruta);
            try {
                fileDatos.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    @Override
    public void setIdServidor(String idServidor) {
        this.idServidor = idServidor;
    }

    @Override
    public void abrirPantallaCine(PantallaTE te) {
        IBrowser browser = te.browser;
        if(browser == null){
            browser = Lizardon.getInstance().getAPI().createBrowser("http://google.es", false);
            browser.loadURL("http://google.es");
            te.browser = browser;
        }
        Minecraft.getInstance().setScreen(new PantallaCine(browser));
    }

    @Override
    public void trackScreen(PantallaTE tes, boolean track) {
        Lizardon.LOGGER.info(track ? "Tracking" : "Untracking" + " screen at " + tes.getBlockPos());
        int idx = -1;
        for(int i = 0; i < screenTracking.size(); i++) {
            if(screenTracking.get(i) == tes) {
                idx = i;
                break;
            }
        }

        if(track) {
            if(idx < 0){
                Lizardon.LOGGER.info("LOG: Tracking screen at " + tes.getBlockPos());
                screenTracking.add(tes);
            }
        } else if(idx >= 0){
            Lizardon.LOGGER.info("LOG: Untracking screen at " + tes.getBlockPos());
            screenTracking.remove(idx);
        }
    }

    @Override
    public void runJS(String js) {
        if(getPadByID(0) == null){
            MessageHelper.enviarMensaje(Minecraft.getInstance().player, "No se ha detectado una SmartRotom en tu inventario");
        }
        getPadByID(0).view.runJS(js,"");
        Minecraft.getInstance().setScreen(new PantallaSmartRotom(getPadByID(0)));
    }


}

