package es.allblue.lizardon.init;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.blocks.*;
import es.allblue.lizardon.items.LizardonItemGroup;
import es.allblue.lizardon.objects.ObjColocable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, Lizardon.MOD_ID);

    public static RegistryObject TWISTER_AMARILLO = registerBlock("twister_amarillo", () -> new Alfombra(Material.WOOL));
    public static RegistryObject TWISTER_AZUL = registerBlock("twister_azul", () -> new Alfombra(Material.WOOL));
    public static RegistryObject TWISTER_ROJO = registerBlock("twister_rojo", () -> new Alfombra(Material.WOOL));
    public static RegistryObject TWISTER_VERDE = registerBlock("twister_verde", () -> new Alfombra(Material.WOOL));
    public static final RegistryObject<Block> FUNKO = registerBlock("funko", () -> new Funko(AbstractBlock.Properties.of(Material.STONE)));


    public static final RegistryObject<Block> PANTALLA = registerBlock("pantalla", () -> new BloquePantalla(AbstractBlock.Properties.of(Material.STONE)));

    public static final RegistryObject<Block> TOCADISCOS = registerBlock("tocadiscos", () -> new BloqueTocadiscos(AbstractBlock.Properties.of(Material.STONE)));

    public static void inicializarComidas(){
        ArrayList<ObjColocable> objetos = ComidasLizardon.getComidas();
        for (ObjColocable objeto : objetos) {
            registrarBloqueLizardon(objeto, () -> new BloqueLizardon(objeto.getHitbox()));
        }
    }

    private static <T extends Block>RegistryObject<T> registrarBloqueLizardon(ObjColocable objeto, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(objeto.getNombre(), block);
        ItemInit.ITEMS.register(objeto.getNombre(), () -> new ObjetoColocable(toReturn.get(), objeto));
        return toReturn;
    }

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(LizardonItemGroup.LIZARDON_GROUP)));
    }

    public static void register(IEventBus eventBus) {
        inicializarComidas();
        BLOCKS.register(eventBus);
    }
}
