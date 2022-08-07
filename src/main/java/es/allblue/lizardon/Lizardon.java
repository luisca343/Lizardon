package es.allblue.lizardon;


import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.client.gui.PantallaSmartRotom;
import es.allblue.lizardon.net.Messages;
import init.ItemInit;
import es.allblue.lizardon.net.LizardonPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.example.BrowserScreen;
import net.montoyo.mcef.example.ModScheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lizardon")
public class Lizardon implements IDisplayHandler, IJSQueryHandler
{

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String BLACKLIST_URL = "http://lizardon.es";
    public static Lizardon INSTANCE = null;
    private Minecraft mc = Minecraft.getInstance();
    public KeyBinding key = new KeyBinding("Open Browser", GLFW.GLFW_KEY_F10, "key.categories.misc");
    private PantallaSmartRotom backup = null;
    private API api;

    public static SharedProxy PROXY = DistExecutor.<SharedProxy>safeRunForDist(() -> ClientProxy::new, () -> SharedProxy::new);


    public void onTickStart(TickEvent.ClientTickEvent event) {
        // Check if our key was pressed
        if(key.isDown() && !(mc.screen instanceof BrowserScreen)) {
            //Display the web browser UI.
            mc.setScreen(hasBackup() ? backup : new BrowserScreen());
            backup = null;
        }
    }

    public void abrirSmartRotom(){
        mc.setScreen(hasBackup() ? backup : new BrowserScreen());
        backup = null;
    }

    public API getAPI() {
        return api;
    }

    public boolean hasBackup() {
    return (backup != null);
}

    public Lizardon() {
        INSTANCE = this;
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        //Grab the API and make sure it isn't null.
        api = MCEFApi.getAPI();
        if(api == null)
            return;

        api.registerScheme("mod", ModScheme.class, true, false, false, true, true, false, false);

        if(api != null) {
            //Register this class to handle onAddressChange and onQuery events
            //api.registerDisplayHandler(this);
            //api.registerJSQueryHandler(this);
        }

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        PROXY.preInit();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(Messages::registryNetworkPackets);

        ClientRegistry.registerKeyBinding(key);
        ItemInit.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        PROXY.init();
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        LizardonPacketHandler.init();
        registrarEventos();
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent ev){
        PacketDistributor.PacketTarget packetDistrutor = PacketDistributor.PLAYER
                .with(
                        () ->
                                (ServerPlayerEntity) ev.getPlayer());
        /*
        CMessageServerInfo message = new CMessageServerInfo(miniservPort);
        Messages.INSTANCE.send(packetDistrutor, message);*/
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the es.allblue.lizardon.client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @Override
    public void onAddressChange(IBrowser browser, String url) {
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
    public boolean handleQuery(IBrowser iBrowser, long l, String s, boolean b, IJSQueryCallback ijsQueryCallback) {
        return false;
    }

    @Override
    public void cancelQuery(IBrowser iBrowser, long l) {

    }

    public void setBackup(PantallaSmartRotom browserScreen) {
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    public void registrarEventos(){
        MinecraftForge.EVENT_BUS.register(new ListenerClass());
    }

    /* Pantallas */

    public static String applyBlacklist(String url) {
        return url;
    }

    public static int getNextAvailablePadID() {
        return 0;
    }

    public static boolean isSiteBlacklisted(String url) {
        return false;
    }

}
