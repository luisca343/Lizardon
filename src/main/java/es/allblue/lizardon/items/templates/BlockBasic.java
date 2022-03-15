package es.allblue.lizardon.items.templates;

import es.allblue.lizardon.Lizardon;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBasic extends Block {
    public BlockBasic(String name, Material materialIn) {
        super(materialIn);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Lizardon.CREATIVE_TAB);
    }
}
