package es.allblue.lizardon.items.crops;

import es.allblue.lizardon.init.BlocksInit;
import es.allblue.lizardon.init.ItemsInit;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class CropAnfekamina extends BlockCrops {
    public static final PropertyInteger CROP_AGE = PropertyInteger.create("age", 0, 7);

    public CropAnfekamina(String name) {
        super();
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
    }

    @Override
    protected Item getSeed() {
        return ItemsInit.semillaAnfekamina;
    }

    @Override
    protected Item getCrop() {
        return ItemsInit.cogollo_anfekamina;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(2) + 1;
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    protected int getBonemealAgeIncrease(World worldIn) {
        return 0;
    }


    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return (state.getBlock() == BlocksInit.BLOQUE_HIERBA_LAVANDA || state.getBlock() == BlocksInit.BLOQUE_ARENA_PALIDA);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return worldIn.canSeeSky(pos);
    }

}
