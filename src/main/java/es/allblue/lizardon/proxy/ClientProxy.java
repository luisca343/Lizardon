package es.allblue.lizardon.proxy;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.event.LizardonEvents;
import es.allblue.lizardon.gui.PantallaPrincipal;
import es.allblue.lizardon.gui.html.PantallaJuegosYTal;
import es.allblue.lizardon.gui.html.RenderizadorJuegosYTal;
import es.allblue.lizardon.gui.mineria.Mineria;
import es.allblue.lizardon.init.ItemsInit;
import es.allblue.lizardon.objects.requests.ActualizarDinero;
import es.allblue.lizardon.objects.requests.PlayerData;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ClientProxy implements IProxy
{


    public class PadData {

        public IBrowser view;
        private boolean isInHotbar;
        private final int id;
        private long lastURLSent;

        private PadData(String url, int id) {
            view = mcef.createBrowser(url);
            view.resize((int) Lizardon.INSTANCE.padResX, (int) Lizardon.INSTANCE.padResY);
            isInHotbar = true;
            this.id = id;
        }

    }
    private API mcef;
    private Minecraft mc;

    private int miniservPort;
    private boolean msClientStarted;
    private final HashMap<Integer, PadData> padMap = new HashMap<>();
    private final ArrayList<PadData> padList = new ArrayList<>();

    public static RenderizadorJuegosYTal renderizador;
    public static PantallaJuegosYTal pantalla;


    private void updatePad(int id, NBTTagCompound tag, boolean isSelected) {
        PadData pd = padMap.get(id);

        if(pd != null)
            pd.isInHotbar = true;
        else if(isSelected && tag.hasKey("PadURL")) {
            pd = new PadData(tag.getString("PadURL"), id);
            padMap.put(id, pd);
            padList.add(pd);
        }
    }

    private void updateInventory(NonNullList<ItemStack> inv, ItemStack heldStack, int cnt) {
        for(int i = 0; i < cnt; i++) {
            ItemStack item = inv.get(i);

            if(item.getItem() == ItemsInit.smart_rotom) {
                NBTTagCompound tag = item.getTagCompound();

                if(tag != null && tag.hasKey("PadID"))
                    updatePad(tag.getInteger("PadID"), tag, item == heldStack);
            }
        }
    }


    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        renderizador  = new RenderizadorJuegosYTal();
        renderizador.onPreInit();

    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        Lizardon.getLogger().info("PROXY DE CLIENTE INICIADO");
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        pantalla = new PantallaJuegosYTal("http://www.google.es");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");
        renderizador.onInit();
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : Lizardon.proxy.getPlayerEntityFromContext(ctx));
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
        // This will never get called on client side
    }

    public void abrirMovil(){
        PantallaPrincipal pantalla = new PantallaPrincipal();
        Minecraft.getMinecraft().displayGuiScreen(pantalla);
    }

    public void abrirVentana(GuiScreen screen){
        Minecraft.getMinecraft().displayGuiScreen(screen);
    }

    @Override
    public void spawnItem(ItemStack itemStack) {

    }

    @Override
    public void abrirVentanaMineria() {
        Minecraft.getMinecraft().displayGuiScreen(new Mineria());
    }


    @Override
    public void abrirVentanaTest(String s) {
        RenderizadorJuegosYTal.INSTANCE.showScreen(s);
    }

    @Override
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerData data = Minecraft.getMinecraft().getCurrentServerData();
        if(data == null || data.serverIP == null) {
            World world = event.player.getEntityWorld();
            String nombre = world.getMinecraftServer().getMOTD().replaceAll("\\s+","_");

            Lizardon.getLogger().info("NOMBRE: "+nombre);
        }
        //loginPlayer(event);
    }


    @Override
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ServerData data = Minecraft.getMinecraft().getCurrentServerData();
        if(data == null || data.serverIP == null) {
            Lizardon.NOMBRE_MUNDO = "LOCAL";
        }else{
            if(data.serverIP !=null){
                Lizardon.NOMBRE_MUNDO = data.serverIP.split(":")[0];
                if(Lizardon.NOMBRE_MUNDO.contains("192.168.8.")){
                    Lizardon.NOMBRE_MUNDO = "79.148.46.4";
                }

                Lizardon.getLogger().info("ZA WARUDO:"+Lizardon.NOMBRE_MUNDO);
            }
        }
    }


    @Override
    public void registerRender(Item item){
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
    }

    @Override
    public void cambiarRenderer() {
    }

    public API getMCEF() {
        return mcef;
    }


    public String pasarParametros(String url) {
        String[] urlParams = new String[0];
        String urlBase = url;
        int numParams = 0;
        if(url.contains("?")){
            urlBase = url.split("\\?")[0];
            String query = url.split("\\?")[1];

            urlParams = query.split("&");
        }

        HashMap<String, String> params = new HashMap<>();
        //params.put("unm", Minecraft.getMinecraft().player.getName());
        params.put("unm", Minecraft.getMinecraft().player.getName());
        params.put("wrl", Lizardon.NOMBRE_MUNDO);


        for(int i = 0; i< urlParams.length; i++){
            String param = urlParams[i];
            params.putIfAbsent(param.split("=")[0],param.split("=")[1]);
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String param = key+"="+value;

            if(numParams==0){
                urlBase+="?"+param;
            }else{
                urlBase+="&"+param;
            }
            numParams++;
        }

        Lizardon.getLogger().info("NAVEGANDO A:" + urlBase);
        return urlBase;
    }


}