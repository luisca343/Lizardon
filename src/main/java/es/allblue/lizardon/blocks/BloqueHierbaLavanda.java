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

public class BloqueHierbaLavanda extends BlockBasic {

    public BloqueHierbaLavanda(String name) {
        super(name, Material.GROUND);
        setHardness(0.3f);
        setSoundType(SoundType.GROUND);
    }
    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
        if(plant.getBlock() == BlocksInit.CROP_ANFEKAMINA || plant.getBlock() == BlocksInit.CROP_LUISCAINA)
            return true;
        return false;
    }
}
