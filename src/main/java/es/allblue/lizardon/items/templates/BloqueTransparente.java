package es.allblue.lizardon.items.templates;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

public class BloqueTransparente extends BlockBasic{
    public BloqueTransparente(String name, Material materialIn) {
        super(name, materialIn);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state)
    {
        return true;
    }

}
