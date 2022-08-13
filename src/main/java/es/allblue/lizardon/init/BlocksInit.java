package es.allblue.lizardon.init;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.blocks.BloqueCemento;
import es.allblue.lizardon.items.templates.BlockBasic;
import es.allblue.lizardon.blocks.BloqueArenaPalida;
import es.allblue.lizardon.blocks.BloqueHierbaLavanda;
import es.allblue.lizardon.blocks.BloqueNievePura;
import es.allblue.lizardon.items.crops.CropAnfekamina;
import es.allblue.lizardon.items.crops.CropLuiscaina;
import es.allblue.lizardon.items.crops.CropNeohuana;
import es.allblue.lizardon.items.templates.BloqueTransparente;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid= Lizardon.MODID)
public class BlocksInit {

    public static Block tutorialBlock;
    public static CropLuiscaina CROP_LUISCAINA;
    public static CropNeohuana CROP_NEOHUANA;
    public static CropAnfekamina CROP_ANFEKAMINA;
    static ArrayList<Block> bloques = new ArrayList<>();

    public static BloqueNievePura BLOQUE_NIEVE_PURA;
    public static BloqueHierbaLavanda BLOQUE_HIERBA_LAVANDA;
    public static BloqueArenaPalida BLOQUE_ARENA_PALIDA;
    public static BloqueCemento BLOQUE_CEMENTO;



    public static void init() {
        CROP_LUISCAINA = new CropLuiscaina("planta_luiscaina");
        CROP_NEOHUANA = new CropNeohuana("planta_neohuana");
        CROP_ANFEKAMINA = new CropAnfekamina("planta_anfekamina");

        bloques.add(CROP_LUISCAINA);
        bloques.add(CROP_NEOHUANA);
        bloques.add(CROP_ANFEKAMINA);

        BLOQUE_CEMENTO = new BloqueCemento("cemento");
        bloques.add(BLOQUE_CEMENTO);

        BLOQUE_NIEVE_PURA = new BloqueNievePura("nieve_pura");
        bloques.add(BLOQUE_NIEVE_PURA);
        BLOQUE_HIERBA_LAVANDA = new BloqueHierbaLavanda("hierba_lavanda");
        bloques.add(BLOQUE_HIERBA_LAVANDA);
        BLOQUE_ARENA_PALIDA = new BloqueArenaPalida("arena_palida");
        bloques.add(BLOQUE_ARENA_PALIDA);

        bloques.add(new BlockBasic("unown_block_ñ", Material.ROCK));
        bloques.add(new BloqueTransparente("test", Material.ROCK));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for(int i = 0; i < bloques.size(); i++){
            event.getRegistry().registerAll(bloques.get(i));

        }
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        for(int i = 0; i < bloques.size(); i++){
            event.getRegistry().registerAll(new ItemBlock(bloques.get(i)).setRegistryName(bloques.get(i).getRegistryName()));
        }
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        for(int i = 0; i < bloques.size(); i++){
            registerRender(Item.getItemFromBlock(bloques.get(i)));
        }

    }

    public static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
    }
}