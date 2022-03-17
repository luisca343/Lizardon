package es.allblue.lizardon;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import es.allblue.lizardon.commands.*;
import es.allblue.lizardon.event.LizardonEvents;
import es.allblue.lizardon.event.LizardonPixelmonEvents;
import es.allblue.lizardon.init.BlocksInit;
import es.allblue.lizardon.init.ItemsInit;
import es.allblue.lizardon.init.PotionsInit;
import es.allblue.lizardon.items.LizardonCreativeTab;
import es.allblue.lizardon.net.MsgQueryHandler;
import es.allblue.lizardon.net.MsgQuery;
import es.allblue.lizardon.net.QuerySonido;
import es.allblue.lizardon.net.QuerySonidoHandler;
import es.allblue.lizardon.objects.DatosUser;
import es.allblue.lizardon.objects.ItemTienda;
import es.allblue.lizardon.pixelmon.CustomPokemon;
import es.allblue.lizardon.pixelmon.LizardonStatsLoader;
import es.allblue.lizardon.proxy.IProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.Logger;
import es.allblue.lizardon.sound.SoundRegisterListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.HashMap;

@Mod(modid = Lizardon.MODID, name = Lizardon.NAME, version = Lizardon.VERSION, dependencies = "required-after:pixelmon")
public class Lizardon {
    public static final String MODID = "lizardon";
    public static final String NAME = "Lizardon";
    public static final String VERSION = "1.0";
    public static final String URL_INICIO = "http://lizardon.es/smart_rotom";
    public static final String URL_BASE = "http://lizardon.es/smart_rotom/menu";

    @SidedProxy(
            clientSide = "es.allblue.lizardon.proxy.ClientProxy",
            serverSide = "es.allblue.lizardon.proxy.ServerProxy"
    )
    public static IProxy proxy;

    public static Lizardon INSTANCE;
    private static Logger logger;
    private static Connection connection;
    public static LizardonCreativeTab CREATIVE_TAB = new LizardonCreativeTab();
    public static SimpleNetworkWrapper NET_HANDLER;
    public static final double PAD_RATIO = 59.0 / 30.0;
    public double padResX;
    public double padResY;
    public static long miniservQuota;
    public static MinecraftServer server;
    public static File carpetaRaiz;
    public static HashMap<String, ItemTienda> itemsTienda;
    public static String NOMBRE_MUNDO;
    public DatosUser datos;

    public int maxResX;
    public int maxResY;

    public String homePage;
    private static Configuration cfg;
    public static File archivoCoches;
    public static String[] coches;


    public static String applyBlacklist(String url) {
        return url;
    }

    /* Config */
    public static int DURACION_TASER;

    public static double PORTAL_MODIFIER = 1.0D;

    public static double NAUSEA_MODIFIER = 1.0D;

    public static boolean NAUSEA_STUMBLING = false;

    public static void cargarConfig() {
        Gson gson = new Gson();

        cfg = new Configuration(new File("./config/Lizardon/Lizardon.cfg"));
        archivoCoches = new File("./config/Lizardon/coches.json");
        
        Property duracionTaser = cfg.get("main", "Duración táser (segundos)", 5);
        duracionTaser.setComment("No sé qué hace esto, pero buehno.");
        DURACION_TASER = duracionTaser.getInt();

        Property portalModifier = cfg.get("main", "Modificador portal", 1.0D);
        portalModifier.setComment("No sé qué hace esto, pero buehno.");
        PORTAL_MODIFIER = portalModifier.getDouble();

        Property nauseaModifier = cfg.get("main", "Modificador nausea", 1.0D);
        nauseaModifier.setComment("No sé qué hace esto, pero buehno.");
        NAUSEA_MODIFIER = nauseaModifier.getDouble();

        Property nauseaStumbling = cfg.get("main", "Tambaleo nausea", false);
        nauseaStumbling.setComment("No sé qué hace esto, pero buehno.");
        NAUSEA_STUMBLING = nauseaStumbling.getBoolean();

        cfg.save();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        logger = event.getModLog();
        carpetaRaiz = new File(event.getModConfigurationDirectory().getParent());
        BlocksInit.init();
        PotionsInit.init();
        cargarConfig();

        ItemsInit.init();

        padResY = 480;
        padResX = padResY * PAD_RATIO;
        miniservQuota = 1024 * 1024L;
        homePage = "mod://webdisplays/main.html";
        this.maxResX = 1920;
        this.maxResY = 1080;

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(this);
        cargarObjetosTiendas();

        MinecraftForge.EVENT_BUS.register(new SoundRegisterListener());

        CustomPokemon pokemon = new CustomPokemon();
        pokemon.insertarPokemon();

        LizardonStatsLoader.cargarStats();
        Pokedex.loadPokedex();

        logger.info("Recargando stats de formas Teras...");
        Pixelmon.EVENT_BUS.register(LizardonPixelmonEvents.class);
        MinecraftForge.EVENT_BUS.register(LizardonEvents.class);

        proxy.init(event);
        NET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("lizardon");

        NET_HANDLER.registerMessage(MsgQueryHandler.class, MsgQuery.class, 0, Side.SERVER);
        NET_HANDLER.registerMessage(QuerySonidoHandler.class, QuerySonido.class, 1, Side.SERVER);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }


    public static void cargarObjetosTiendas() {
        itemsTienda = new HashMap<>();
        try {
            OkHttpClient client = new OkHttpClient();
            // put your json here
            Request request = new Request.Builder()
                    .url("http://api.lizardon.es/pixelmon/datosTiendas")
                    .get()
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                String resStr = response.body().string();

                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(resStr);
                JsonObject json = root.getAsJsonObject();


                if (json.has("items")) {
                    JsonArray array = JsonUtils.getJsonArray(json, "items");
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject itemNPC = array.get(i).getAsJsonObject();
                        String name = itemNPC.get("name").getAsString();

                        int compra = 0;
                        int venta = 0;
                        if (itemNPC.has("buy")) {
                            compra = itemNPC.get("buy").getAsInt();
                        }
                        if (itemNPC.has("sell")) {
                            venta = itemNPC.get("sell").getAsInt();
                        }
                        itemsTienda.put(name, new ItemTienda(name, compra, venta));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadModels() {
        try {
            Method m = PixelmonModelRegistry.class.getDeclaredMethod("init", new Class[0]);
            m.setAccessible(true);
            m.invoke(null, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.SERVER)
    @EventHandler
    public void init(FMLServerStartingEvent event) {
        event.registerServerCommand(new Gimnasio());
        event.registerServerCommand(new LizardonReload());
        event.registerServerCommand(new DesbloquearApp());
        event.registerServerCommand(new GuardarCoche());
        event.registerServerCommand(new Test());
        server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (server != null) {
            NOMBRE_MUNDO = FMLCommonHandler.instance().getMinecraftServerInstance().getServerHostname().split(":")[0];

            if (NOMBRE_MUNDO.contains("192.168.8.")) {
                NOMBRE_MUNDO = "79.148.46.4";
            }
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Lizardon getInstance() {
        return INSTANCE;
    }

    public static IProxy getProxy() {
        return proxy;
    }

    public static Connection getConnection() {
        return connection;
    }
}
