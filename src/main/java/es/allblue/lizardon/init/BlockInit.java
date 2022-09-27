package es.allblue.lizardon.init;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.items.LizardonItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, Lizardon.MOD_ID);

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> object = BLOCKS.register(name, block);
        registerBlockItem(name, object);
        return object;

    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(LizardonItemGroup.LIZARDON_GROUP)));
    }
}
