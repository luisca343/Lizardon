package es.allblue.lizardon.fluids;

import es.allblue.lizardon.init.FluidInit;
import es.allblue.lizardon.init.ItemInit;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Random;

public abstract class AguasTermalesFluid extends ForgeFlowingFluid {
    protected AguasTermalesFluid() {
        super(new ForgeFlowingFluid.Properties(
                FluidInit.AGUAS_TERMALES_SOURCE, FluidInit.AGUAS_TERMALES_FLOWING, FluidAttributes.builder(
                        FluidInit.WATER_STILL, FluidInit.WATER_FLOW).overlay(FluidInit.WATER_OVERLAY).color(0xFF40d9f7))
                .block(FluidInit.AGUAS_TERMALES_BLOCK).bucket(ItemInit.CUBO_AGUAS_TERMALES).canMultiply()
        );
    }


    @Override
    protected void animateTick(World world, BlockPos pos, FluidState state, Random random) {
        System.out.println("animateTick");

        BlockPos arriba = pos.above();

            world.addParticle(ParticleTypes.SMOKE, arriba.getX() + 0.1F + random.nextFloat() * 0.8F,
                    arriba.getY() + 0.5F, arriba.getZ() + 0.1F + random.nextFloat() * 0.8F, 0.0D, 0.025 + random.nextFloat() / 250.0F, 0.0D);

    }





}
