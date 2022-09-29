package es.allblue.lizardon;


import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.EffectTypeAdapter;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.client.gui.PantallaSmartRotom;
import es.allblue.lizardon.event.MisionesCaza;
import es.allblue.lizardon.event.ModEvents;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.pixelmon.attacks.TestAttack;
import es.allblue.lizardon.init.ItemInit;
import es.allblue.lizardon.net.LizardonPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.example.ModScheme;
import noppes.npcs.api.NpcAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lizardon")
public class Lizardon
{
    // Directly reference a log4j logger.
    public static final double PAD_RATIO = 59.0 / 30.0;
    public static final String SMARTROTOM_HOME = "http://localhost:3000/smartrotom";
    public String homePage;
    public double padResX;
    public double padResY;
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String BLACKLIST_URL = "http://lizardon.es";
    public static Lizardon INSTANCE = null;
    private API api;
    public static String MOD_ID = "lizardon";
    public static String HEADER_MENSAJE = TextFormatting.BLUE + "" + TextFormatting.BOLD+"[Lizardon]: ";

    public static SharedProxy PROXY = DistExecutor.<SharedProxy>safeRunForDist(() -> ClientProxy::new, () -> SharedProxy::new);


    public API getAPI() {
        return api;
    }

    public Lizardon() {
        INSTANCE = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::end);

        padResY = 480;
        padResX = padResY * PAD_RATIO;


        NpcAPI npcAPI = NpcAPI.Instance();
        Lizardon.getLogger().info("TESTES");
        npcAPI.events().register(ModEvents.class);

        //Grab the API and make sure it isn't null.
        api = MCEFApi.getAPI();
        if(api == null)
            return;

        api.registerScheme("lizardon", ModScheme.class, true, false, false, true, true, false, false);

        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);
        PROXY.preInit();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(Messages::registryNetworkPackets);

        ItemInit.register(eventBus);
    }
    public void setup(final FMLCommonSetupEvent event)
    {
        Pixelmon.EVENT_BUS.register(new MisionesCaza());
        // some preinit code
        //PROXY.es.allblue.lizardon.init();
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        /*
        LOGGER.info(TestAbility.class.getSimpleName());
        AbilityRegistry.register("es.allblue.lizardon.pixelmon.abilities.TestAbility");
        Ability ability = AbilityRegistry.getAbility("TestAbility").get();
        LOGGER.info(ability.getName());*/

        EffectTypeAdapter.EFFECTS.put("TestAttack", TestAttack.class);
        LizardonPacketHandler.init();

        LOGGER.info("DESCARGANDO MUSICA");
        try{
            String url = "http://i.lizardon.es/pixelmon/sonido/denden.mp3";
            String[] partes = url.split("/");
            String carpetaMusica = "Lizardon/musica/";
            File directorio = new File(carpetaMusica);
            if(!directorio.exists()){
                Files.createDirectories(Paths.get(carpetaMusica));
            }

            URLConnection connection = new 	URL(url).openConnection();

            connection.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            InputStream is = connection.getInputStream();

            OutputStream outstream = new FileOutputStream(new File(carpetaMusica + partes[partes.length-1]));

            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, length);
            }
            System.out.println("mp3 download completed.");
            outstream.close();
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }
    }

    public void end (FMLLoadCompleteEvent event){
        PROXY.end(event);
    }


    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent ev){
        PacketDistributor.PacketTarget packetDistrutor = PacketDistributor.PLAYER
                .with(
                        () ->
                                (ServerPlayerEntity) ev.getPlayer());

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
    /* Pantallas */

    public static String applyBlacklist(String url) {
        if(url.contains("smartrotom"))
            return "Smart Rotom";
        return url;
    }

    public static int getNextAvailablePadID() {
        return 0;
    }

    public static boolean isSiteBlacklisted(String url) {
        return false;
    }

    public static Logger getLogger(){
        return LOGGER;
    }

}
