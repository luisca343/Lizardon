package es.allblue.lizardon.blocks;

import es.allblue.lizardon.init.BlocksInit;
import es.allblue.lizardon.items.templates.BlockBasic;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public class BloqueArenaPalida extends BlockBasic {

    public BloqueArenaPalida(String name) {
        super(name, Material.SAND);
        setHardness(0.15f);
        setSoundType(SoundType.SAND);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
        if(plant.getBlock() == BlocksInit.CROP_ANFEKAMINA || plant.getBlock() == BlocksInit.CROP_NEOHUANA)
            return true;
        return false;
    }
}
