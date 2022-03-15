package es.allblue.lizardon.init;

import com.pixelmonmod.pixelmon.Pixelmon;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.items.KitMineria;
import es.allblue.lizardon.items.PiedraEspiritu;
import es.allblue.lizardon.items.armor.SombreroBase;
import es.allblue.lizardon.items.crops.SemillaAnfekamina;
import es.allblue.lizardon.items.crops.SemillaLuiscaina;
import es.allblue.lizardon.items.crops.SemillaNeohuana;
import es.allblue.lizardon.items.rol.CocheMision;
import es.allblue.lizardon.items.rol.Porra;
import es.allblue.lizardon.items.rol.Taser;
import es.allblue.lizardon.items.templates.FoodItemBasic;
import es.allblue.lizardon.items.templates.ItemBasic;
import es.allblue.lizardon.items.templates.DrinkItemBasic;
import es.allblue.lizardon.items.SmartRotom;
import es.allblue.lizardon.items.templates.ShieldBasic;
import es.allblue.lizardon.objects.mineria.RecompensaMina;
import es.allblue.lizardon.util.LootCollection;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid= Lizardon.MODID)
public class ItemsInit {

    public static SemillaLuiscaina semillaLuiscaina;
    public static SemillaAnfekamina semillaAnfekamina;
    public static SemillaNeohuana semillaNeohuana;
    public static Item hojas_luiscaina;
    public static Item cogollo_anfekamina;
    public static Item hojas_neohuana;
    public static SmartRotom smart_rotom;
    public static PiedraEspiritu piedra_espiritu;
    static ArrayList<Item> items = new ArrayList<>();
    public static LootCollection<RecompensaMina> recompensas;

    public static Item coche;

    public static void instanciar(){
        /* Instanciar objetos */
        hojas_luiscaina = new ItemBasic("hojas_luiscaina").setIlegal("Hojas de planta psicotrópica provinientes de un lugar lejano. Poca gente sabe cómo cultivarla, por ello es muy codiciada.");
        semillaLuiscaina = new SemillaLuiscaina("semillas_luiscaina");
        hojas_neohuana = new ItemBasic("hojas_neohuana").setIlegal("Hojas de planta psicotrópica provinientes de un lugar lejano. Poca gente sabe cómo cultivarla, por ello es muy codiciada.");
        semillaNeohuana = new SemillaNeohuana("semillas_neohuana");
        cogollo_anfekamina = new ItemBasic("cogollo_anfekamina").setIlegal("Cogollo de planta psicotrópica provinientes de un lugar lejano. Poca gente sabe cómo cultivarla, por ello es muy codiciada.");
        semillaAnfekamina = new SemillaAnfekamina("semillas_anfekamina");
        smart_rotom = new SmartRotom("smart_rotom");
        piedra_espiritu = new PiedraEspiritu("piedra_espiritu");
        coche = new CocheMision("coche").setMaxStackSize(1);
    }


    public static void init() {
        instanciar();
        /* Inicializar bebidas */
        items.add(new DrinkItemBasic("ay_mi_madre").addLore("Siente el poder del Dios Potasio con esta refrescante bebida.").addPotionEffect(new PotionEffect(MobEffects.STRENGTH,600,0),.2f).addPotionEffect(new PotionEffect(MobEffects.RESISTANCE,600,1),.2f).addPotionEffect(new PotionEffect(MobEffects.SPEED,600,1),.2f).addPotionEffect(new PotionEffect(MobEffects.HASTE,600,1),.2f).addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST,600,1),.2f).addPotionEffect(new PotionEffect(MobEffects.LUCK,600,1),.2f));
        items.add(new DrinkItemBasic("flameado_de_moe").addLore("Receta totalmente original de §mMoe Szyslak§r§2 Kamina").addPotionEffect(new PotionEffect(MobEffects.GLOWING,600,0),1.0f).addPotionEffect(new PotionEffect(MobEffects.HASTE,600,1),1.0f));
        items.add(new DrinkItemBasic("garchupito").addLore("Electrizante chupito preparado con mucho amor. ¡GARCHU!").addPotionEffect(new PotionEffect(MobEffects.GLOWING,600,0),1.0f));
        items.add(new DrinkItemBasic("itamilk").addLore("Los ganaderos no han querido revelar de qué animal procede.").addPotionEffect(new PotionEffect(MobEffects.RESISTANCE,600,0),1.0f).setMaxStackSize(16));
        items.add(new DrinkItemBasic("luiscacola").addLore("No lleva nada de luiscaína.").addPotionEffect(new PotionEffect(MobEffects.SPEED,600,1),1.0f));
        items.add(new DrinkItemBasic("neofabirra").addLore("Con un toque de leche de oveja.").addPotionEffect(new PotionEffect(MobEffects.NAUSEA,600,0),1.0f));
        items.add(new DrinkItemBasic("red_tauros").addLore("Te hace volar. Aunque los Tauros no vuelen.").addPotionEffect(new PotionEffect(MobEffects.LEVITATION,600,0),1.0f));
        items.add(new DrinkItemBasic("tojenebra").addLore("23 son los segundos que tardarás en perder el conocimiento.").addPotionEffect(new PotionEffect(MobEffects.NAUSEA,1200,4),1.0f));
        items.add(new DrinkItemBasic("tsukila").addLore("Tan ardiente que te hará escupir fuego.").addPotionEffect(new PotionEffect(MobEffects.NAUSEA,600,2),1.0f));
        items.add(new DrinkItemBasic("uriebirra").addLore("Su receta es el secreto peor guardado de la historia.").addPotionEffect(new PotionEffect(MobEffects.NAUSEA,600,0),1.0f));
        items.add(new DrinkItemBasic("leyiax").addLore("Si la bebes eres mi esclavo UwU.").addPotionEffect(new PotionEffect(MobEffects.NAUSEA,600,0),1.0f));
        /* Inicializar comidas */
        items.add(new FoodItemBasic("aguisado",11,15,false).addLore("A base de carne de Miltank aderezado con plantas... Aromáticas."));
        items.add(new FoodItemBasic("chemoso_de_queso",2,3,false).addLore("Hmmmm... Queso..."));
        items.add(new FoodItemBasic("cpizza",5,10,false).addLore("La crítica la ha puntuado con un treh, la receta es más compleja de lo que debería."));
        items.add(new FoodItemBasic("dulce_de_lehe",3,7,false).addLore("Hecho con mucho amor. Y caca."));
        items.add(new FoodItemBasic("fiifdeua",8,5,false).addLore("Parece una paella, pero esto es un mundo de bloques, al fin y al cabo, y ya sabéis, no es más rico el que más tiene, sino el que menos necesita, porque a caballo regalado no le mires el golpe de remo. Luisca Coelho - Vendedor de Opel Corsa."));
        items.add(new FoodItemBasic("flankuslop",2,2,false).addLore("Creado por un chef ruso con un brazo roto."));
        items.add(new FoodItemBasic("lucasitos",1,1,false).addLore("Incluye caramelos de 535 colores diferentes."));
        items.add(new FoodItemBasic("potimerienda",10,14,false).addLore("Se dice que quien come este plato se vuelve más hermoso.").setMaxStackSize(16));
        items.add(new FoodItemBasic("nachos_neouacamole",4,6,false).addLore("El mejor plato del otro lado del muro."));
        items.add(new FoodItemBasic("rarisotto",7,14,false).addLore("Se dice que es el plato más exquisito del Valhalla."));
        items.add(new FoodItemBasic("shurros",3,5,false).addLore("A pesar de su nombre, tienen buen sabor."));
        items.add(new FoodItemBasic("yakitoki",4,5,true).addLore("Brocheta de alitas celestiales."));
        items.add(new FoodItemBasic("yastekomo",6,6,true).addLore("Yast some normal noodles."));

        /*  */
        items.add(smart_rotom);
        items.add(new KitMineria("kit_mineria"));
        items.add(piedra_espiritu);

        /* Policía */
        items.add(new Taser("taser"));
        items.add(new Porra("porra"));
        items.add(new ShieldBasic("escudo_anti_leya"));
        items.add(new ShieldBasic("escudo_antidisturbios"));

        /* Cascos */
        //items.add(new SombreroBase("orejas_gato"));
        items.add(new SombreroBase("caca_de_waifu"));
        items.add(new SombreroBase("sombrero_paja"));
        items.add(new SombreroBase("gorro_detective"));

        /* Inicializar plantas */
        items.add(semillaLuiscaina);
        items.add(hojas_luiscaina);
        items.add(semillaNeohuana);
        items.add(hojas_neohuana);
        items.add(semillaAnfekamina);
        items.add(cogollo_anfekamina);
        items.add(new ItemBasic("luiscaina"));
        items.add(new ItemBasic("gachacoin"));


        items.add(coche);
        generarRecompensas();


    }

public static void generarRecompensas(){
    recompensas = new LootCollection<>();

    /* GEMAS */
    cargarItemMina(500,"gema_azul_peque",2,2, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_azul_peque.png");
    cargarItemMina(250,"gema_azul_grande",3,3, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_azul_grande.png");
    cargarItemMina(500,"gema_roja_peque",2,2, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_roja_peque.png");
    cargarItemMina(250,"gema_roja_grande",3,3, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_roja_grande.png");
    cargarItemMina(500,"gema_verde_peque",2,2, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_verde_peque.png");
    cargarItemMina(250,"gema_verde_grande",3,3, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_verde_grande.png");
    cargarItemMina(250,"gema_prisma_peque",2,2, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_prisma_peque.png");
    cargarItemMina(100,"gema_prisma_grande",3,3, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_prisma_grande.png");
    cargarItemMina(250,"gema_blanca_peque",2,2, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_blanca_peque.png");
    cargarItemMina(100,"gema_blanca_grande",3,3, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/gema_blanca_grande.png");

    /* TABLAS */
    cargarItemMina(50,"draco_plate",3,3,Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/draco_plate.png");
    cargarItemMina(50,"dread_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/dread_plate.png");
    cargarItemMina(50,"earth_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/earth_plate.png");
    cargarItemMina(50,"fist_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/fist_plate.png");
    cargarItemMina(50,"flame_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/flame_plate.png");
    cargarItemMina(50,"icicle_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/icicle_plate.png");
    cargarItemMina(50,"insect_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/insect_plate.png");
    cargarItemMina(50,"iron_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/iron_plate.png");
    cargarItemMina(50,"meadow_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/meadow_plate.png");
    cargarItemMina(50,"mind_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/mind_plate.png");
    cargarItemMina(50,"sky_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/sky_plate.png");
    cargarItemMina(50,"splash_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/splash_plate.png");
    cargarItemMina(50,"spooky_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/spooky_plate.png");
    cargarItemMina(50,"stone_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/stone_plate.png");
    cargarItemMina(50,"toxic_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/toxic_plate.png");
    cargarItemMina(50,"zap_plate",3,3, Pixelmon.MODID, Lizardon.MODID, "textures/items/subsuelo/zap_plate.png");

    /* Fósiles */
    cargarItemMina(250,"piedra_teras",2,2, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/piedra_teras.png");
    cargarItemMina(50,"covered_fossil_0",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredhelixfossil.png");
    cargarItemMina(50,"covered_fossil_1",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/covereddomefossil.png");
    cargarItemMina(50,"covered_fossil_2",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredoldamber.png");
    cargarItemMina(50,"covered_fossil_3",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredrootfossil.png");
    cargarItemMina(50,"covered_fossil_4",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredclawfossil.png");
    cargarItemMina(50,"covered_fossil_5",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredskullfossil.png");
    cargarItemMina(50,"covered_fossil_6",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredarmorfossil.png");
    cargarItemMina(50,"covered_fossil_7",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredcoverfossil.png");
    cargarItemMina(50,"covered_fossil_8",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredplumefossil.png");
    cargarItemMina(50,"covered_fossil_9",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredjawfossil.png");
    cargarItemMina(50,"covered_fossil_10",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredsailfossil.png");
    cargarItemMina(50,"covered_fossil_11",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredbirdfossil.png");
    cargarItemMina(50,"covered_fossil_12",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/coveredfishfossil.png");
    cargarItemMina(50,"covered_fossil_13",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/covereddrakefossil.png");
    cargarItemMina(50,"covered_fossil_14",3,3, Pixelmon.MODID, Pixelmon.MODID, "textures/items/fossils/covereddinofossil.png");

    cargarItemMina(100,"piedra_espiritu",2,2, Lizardon.MODID, Lizardon.MODID, "textures/items/subsuelo/piedra_espiritu.png");




    }

    public static void cargarItemMina(int peso,String name, int ancho, int alto, String modId, String modResource, String textura){
        Item item;
        if(!modId.equals(Pixelmon.MODID) && name!="piedra_espiritu"){
            item = new ItemBasic(name);
            items.add(item);
        }
        recompensas.add(peso,new RecompensaMina(name,ancho,alto, modId, modResource, textura));
    }



    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for(int i = 0; i < items.size(); i++){
            event.getRegistry().register(items.get(i));
        }
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        for(int i = 0; i < items.size(); i++){
            registerRender(items.get(i));
        }
    }

    private static void registerRender(Item item) {
        Lizardon.INSTANCE.getProxy().registerRender(item);
    }
}