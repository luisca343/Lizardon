package es.allblue.lizardon.blocks;

import es.allblue.lizardon.init.FluidInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.UUID;

public class BloqueAguasTermales extends FlowingFluidBlock {
    public BloqueAguasTermales() {
        super(FluidInit.AGUAS_TERMALES_SOURCE, AbstractBlock.Properties.of(Material.WATER));
    }



    @Override
    public void entityInside(BlockState state, World world, BlockPos post, Entity entity) {
        super.entityInside(state, world, post, entity);

        if(world.isClientSide() && entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entity;
            if(!player.hasEffect(Effects.REGENERATION)){
                player.addEffect(new net.minecraft.potion.EffectInstance(Effects.REGENERATION, 100, 1));
            }
        }
    }
}
