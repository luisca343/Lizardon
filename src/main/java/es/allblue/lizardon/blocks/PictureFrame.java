package es.allblue.lizardon.blocks;

import es.allblue.lizardon.init.TileEntityInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import team.creative.creativecore.common.util.math.base.Facing;
import team.creative.creativecore.common.util.math.box.AlignedBox;

import javax.annotation.Nullable;


public class PictureFrame extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final float frameThickness = 0.031F;
    public PictureFrame(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }


    public static AlignedBox box(Direction direction) {
        // REVISAR
        Facing facing = Facing.get(direction.ordinal());
        AlignedBox box = new AlignedBox();
        if (facing.positive)
            box.setMax(facing.axis, frameThickness);
        else
            box.setMin(facing.axis, 1 - frameThickness);
        return box;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityInit.FRAME.get().create();

    }

}