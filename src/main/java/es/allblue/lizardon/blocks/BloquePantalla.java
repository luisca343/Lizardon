package es.allblue.lizardon.blocks;

import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.tileentity.PantallaTE;
import es.allblue.lizardon.util.BlockSide;
import es.allblue.lizardon.util.MessageUtil;
import es.allblue.lizardon.util.Multiblock;
import es.allblue.lizardon.util.vector.Vector2i;
import es.allblue.lizardon.util.vector.Vector3i;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.UUID;


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



    /*
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        // get the facing value from the block
        return new PantallaTE(state.getValue(FACING));
    }*/


    // @Override on right click
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos position, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        BlockSide side = BlockSide.values()[hit.getDirection().ordinal()];

        Vector3i pos = new Vector3i(position);
        Multiblock.findOrigin(world, pos, side, null);

        MessageUtil.enviarMensaje(player, "Pos: " + pos.toString() + " Side: " + side.toString());

        PantallaTE te = (PantallaTE) world.getBlockEntity(pos.toBlock());
        Vector2i size = Multiblock.measure(world, pos, side);

        if (size.x < 2 && size.y < 2) {
            player.sendMessage(new StringTextComponent("Ta pequeño"), UUID.randomUUID());
            return ActionResultType.SUCCESS;
        }

        if (size.x > 10 || size.y > 10) {
            player.sendMessage(new StringTextComponent("Ta grande"), UUID.randomUUID());
            return ActionResultType.SUCCESS;
        }

        Vector3i err = Multiblock.check(world, pos, size, side);
        if (err != null) {
            player.sendMessage(new StringTextComponent("No vale illo"), UUID.randomUUID());
            return ActionResultType.SUCCESS;
        }

        boolean created = false;

        String msg = String.format("Player %s (UUID %s) created a screen at %s of size %dx%d", player.getName(), player.getGameProfile().getId().toString(), pos.toString(), size.x, size.y);
        player.sendMessage(new StringTextComponent(msg), UUID.randomUUID());



        if (te == null) {
            MessageUtil.enviarMensaje(player, "No existe un TE");
            BlockPos bp = pos.toBlock();
            te =  new PantallaTE(state.getValue(FACING));
            world.setBlockEntity(bp, te);
            created = true;
        }else{
            MessageUtil.enviarMensaje(player, "Ya existe un TE");
        }

        te.setDimensions(size.x, size.y);
        Lizardon.PROXY.abrirPantallaCine(te);


        /*
        if (world.isClientSide) {
            PantallaTE te2 = (PantallaTE) world.getBlockEntity(position);
            Lizardon.PROXY.abrirPantallaCine(te2);
        }*/
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
