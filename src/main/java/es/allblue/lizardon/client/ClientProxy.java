package es.allblue.lizardon.client;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.gui.PantallaSmartRotom;
import es.allblue.lizardon.client.renders.IItemRenderer;
import es.allblue.lizardon.client.renders.MinePadRenderer;
import es.allblue.lizardon.SharedProxy;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.server.SMessagePadCtrl;
import es.allblue.lizardon.util.QueryHelper;
import init.ItemInit;
import net.minecraft.client.Minecraft;
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
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "lizardon", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientProxy extends SharedProxy  implements IDisplayHandler, IJSQueryHandler {
    private net.montoyo.mcef.api.API mcef;
    public final HashMap<Integer, PadData> padMap = new HashMap<>();
    public final ArrayList<PadData> padList = new ArrayList<>();
    private MinePadRenderer minePadRenderer = new MinePadRenderer();
    private PantallaSmartRotom backup = null;
    private Minecraft mc = Minecraft.getInstance();

    private int minePadTickCounter = 0;

    public class PadData {

        public IBrowser view;
        private boolean isInHotbar;
        private final int id;
        private long lastURLSent;

    private PadData(String url, int id) {
        view = mcef.createBrowser(Lizardon.INSTANCE.SMARTROTOM_HOME);
        view.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        //view.resize((int) Lizardon.INSTANCE.padResX, (int)  Lizardon.INSTANCE.padResX);
        isInHotbar = true;
        this.id = id;
    }
}

    @SubscribeEvent
    public void onRenderPlayerHand(RenderHandEvent ev) {
        Item item = ev.getItemStack().getItem();
        IItemRenderer renderer;

        if(ItemInit.SMARTROTOM.isPresent()) {
            if (item == ItemInit.SMARTROTOM.get()){
                renderer = minePadRenderer;
            }
            else
                return;
            renderer.render(ev.getMatrixStack(), ev.getItemStack(), (ev.getHand() == Hand.MAIN_HAND) ? 1.0f : -1.0f, ev.getSwingProgress(), ev.getEquipProgress(), ev.getBuffers(), ev.getLight());
            ev.setCanceled(true);
        }
    }

    @Override
    public void preInit() {
        mc = Minecraft.getInstance();
        MinecraftForge.EVENT_BUS.register(this);

        mcef = Lizardon.INSTANCE.getAPI();
        if(mcef != null)
            mcef.registerScheme("lizardon", null, true, false, false, true, true, false, false);
    }

    public void end (FMLLoadCompleteEvent event){
        API api = Lizardon.INSTANCE.getAPI();
        if(api != null) {
            //Register this class to handle onAddressChange and onQuery events
            //api.registerDisplayHandler(this);
            System.out.println("Registering JSQueryHandler...");
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
        }
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
                        pd.view.loadURL(Lizardon.BLACKLIST_URL);
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
        System.out.println("SCRIPT CAANCELAD");
    }


}

