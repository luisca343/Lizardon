package es.allblue.lizardon;


import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.EffectTypeAdapter;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.event.*;
import es.allblue.lizardon.init.*;
import es.allblue.lizardon.integration.Integrations;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.PacketHandler;
import es.allblue.lizardon.net.video.ScreenManager;
import es.allblue.lizardon.objects.serverdata.LizardonConfig;
import es.allblue.lizardon.objects.karts.CarreraManager;
import es.allblue.lizardon.pixelmon.attacks.DesenvaineSubito;
import es.allblue.lizardon.pixelmon.attacks.TestAttack;
import es.allblue.lizardon.pixelmon.battle.LizardonBattleController;
import es.allblue.lizardon.tileentity.FunkoTERenderer;
import es.allblue.lizardon.tileentity.PantallaRenderer;
import es.allblue.lizardon.util.music.LizardonSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.example.ModScheme;
import noppes.npcs.api.NpcAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import es.allblue.lizardon.client.renders.TVBlockRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lizardon")
public class Lizardon
{
    // Directly reference a log4j logger.

    // SmartRotom START
    public static final double PAD_RATIO = 59.0 / 30.0;
    public static Gson GSON = new Gson();
    //public  static final String SMARTROTOM_HOME = "http://lizardon.es/smartrotom";
    // public static final String SMARTROTOM_HOME = "http://localhost:3000/smartrotom";
    public double padResX;
    public double padResY;

    public double avDist100 = 10.0;
    public double avDist0 = 30.0;
    public float ytVolume = 100.0f;

    // SmartRotom END
    public static final Logger LOGGER = LogManager.getLogger();
    //public static final String BLACKLIST_URL = "http://lizardon.es/smartrotom";

    public static Lizardon INSTANCE = null;
    public static LizardonBattleController lbc;
    private API api;
    public static String MOD_ID = "lizardon";
    public static String HEADER_MENSAJE = TextFormatting.BLUE + "" + TextFormatting.BOLD+"[Lizardon]: ";

    public static SharedProxy PROXY = DistExecutor.<SharedProxy>safeRunForDist(() -> ClientProxy::new, () -> SharedProxy::new);

    public static CarreraManager carreraManager;

    public static LizardonConfig config;

    public API getAPI() {
        return api;
    }

    public static Lizardon getInstance() {
        return INSTANCE;
    }

    public static Logger getLogger(){
        return LOGGER;
    }

    public static LizardonBattleController getLBC() {
        return lbc;
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
        npcAPI.events().register(CustomNPCsEvents.class);

        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(Messages::registryNetworkPackets);


        ItemInit.register(eventBus);
        BlockInit.register(eventBus);
        FluidInit.register(eventBus);

        TileEntityInit.register(eventBus);
        ModBiomes.register(eventBus);
        LizardonSoundEvents.register(eventBus);
    }
    public void setup(final FMLCommonSetupEvent event)
    {
        Pixelmon.EVENT_BUS.register(new MisionesCaza());
        Pixelmon.EVENT_BUS.register(new PixelmonEvents());
        Pixelmon.EVENT_BUS.register(new LizardonBattleEvent());
        Pixelmon.EVENT_BUS.register(new LizardonBattleLogEvent());
        // some preinit code
        //PROXY.es.allblue.lizardon.init();
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        Lizardon.PROXY.crearArchivo("config.json");


        ModBiomes.generateBiomes();
        //Grab the API and make sure it isn't null.
        api = MCEFApi.getAPI();

        if(api == null){
            Lizardon.LOGGER.error("API NO FUNKA");
            return;
        }else{
            Lizardon.LOGGER.info("API FUNKA");
        }

        api.registerScheme("lizardon", ModScheme.class, true, false, false, true, true, false, false);

        PROXY.preInit();
        lbc = new LizardonBattleController();


        Integrations.registerBackpackIntegrations();


        EffectTypeAdapter.EFFECTS.put("TestAttack", TestAttack.class);
        EffectTypeAdapter.EFFECTS.put("DesenvaineSubito", DesenvaineSubito.class);


        event.enqueueWork(CommonHandler::setup);
    }

    public void end (FMLLoadCompleteEvent event){
        PermissionAPI.registerNode("lizardon.frames.video", DefaultPermissionLevel.OP, "Permission to use video frames");
        PROXY.end(event);
    }


    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the es.allblue.lizardon.client
        // ClientRegistry.bindTileEntityRenderer(TileEntityInit.FUNKO_TE.get(), FunkoRenderer::new);

        ClientRegistry.bindTileEntityRenderer(TileEntityInit.FUNKO_TE.get(), FunkoTERenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityInit.TEST_PANTALLA.get(), PantallaRenderer::new);


        RenderTypeLookup.setRenderLayer(FluidInit.AGUAS_TERMALES_SOURCE.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(FluidInit.AGUAS_TERMALES_FLOWING.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(FluidInit.AGUAS_TERMALES_BLOCK.get(), RenderType.translucent());


        RenderTypeLookup.setRenderLayer(BlockInit.TVBLOCK.get(), RenderType.cutout());
        ClientRegistry.bindTileEntityRenderer(TileEntityInit.TV_TE.get(), TVBlockRenderer::new);


        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        /*
        SlotTypePreset[] slots = {
                SlotTypePreset.HEAD, SlotTypePreset.NECKLACE, SlotTypePreset.BACK, SlotTypePreset.BODY,
                SlotTypePreset.HANDS, SlotTypePreset.RING, SlotTypePreset.CHARM
        };
        List<SlotTypeMessage.Builder> builders = new ArrayList<>();
        for (SlotTypePreset slot : slots) {
            SlotTypeMessage.Builder builder = slot.getMessageBuilder();
            if (slot == SlotTypePreset.RING) {
                builder.size(2);
            }
            builders.add(builder);
        }
        for (SlotTypeMessage.Builder builder : builders) {
            SlotTypeMessage message = builder.build();
            InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                    ()->message);
        }*/

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
        String carpetaMusica = "Lizardon";
        File directorio = new File(carpetaMusica);
        if(!directorio.exists()){
            try {
                Files.createDirectories(Paths.get("/Lizardon"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void setBackup() {
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


}
