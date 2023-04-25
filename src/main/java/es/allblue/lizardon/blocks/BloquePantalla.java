package es.allblue.lizardon.blocks;

import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.tileentity.PantallaTE;
import es.allblue.lizardon.util.BlockSide;
import es.allblue.lizardon.util.Multiblock;
import es.allblue.lizardon.util.vector.Vector3i;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;


public class BloquePantalla extends HorizontalBlock {
    public BloquePantalla(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        // get the facing value from the block
        return new PantallaTE(state.getValue(FACING));
    }


    // @Override on right click
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide) {
            PantallaTE te = (PantallaTE) worldIn.getBlockEntity(pos);
            Lizardon.PROXY.abrirPantallaCine(te);
        }
        return ActionResultType.SUCCESS;
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        if (!world.isClientSide) {

            Vector3i vec1 = new Vector3i(pos);
            System.out.println("Vec1: " + vec1);
            /*
            for (BlockSide side : BlockSide.values()) {
                Vector3i vec = new Vector3i(pos);
                System.out.println("Vec antes: " + vec);
                Multiblock.findOrigin(world, vec, side, null);

                System.out.println("Vec despues: " + vec);

                PantallaTE tes = (PantallaTE) world.getBlockEntity(vec.toBlock());

                Direction facing = Direction.from2DDataValue(side.reverse().ordinal());
                vec.sub(pos.getX(), pos.getY(), pos.getZ()).neg();

            }*/

            if(block != this){
                System.out.println("Neighbor changed");
            }else {
                System.out.println("I'm the neighbor");
            }
        }
    }
}
