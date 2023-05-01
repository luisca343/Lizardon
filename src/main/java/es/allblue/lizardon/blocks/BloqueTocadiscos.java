package es.allblue.lizardon.blocks;

import es.allblue.lizardon.init.ItemInit;
import es.allblue.lizardon.tileentity.TocadiscosTE;
import es.allblue.lizardon.util.MessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;


public class BloqueTocadiscos extends HorizontalBlock {
    public BloqueTocadiscos(Properties properties) {
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
        return new TocadiscosTE(state.getValue(FACING));
    }


    // @Override on right click
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos position, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TocadiscosTE te = (TocadiscosTE) world.getBlockEntity(position);
        if (te == null) {
            MessageUtil.enviarMensaje(player, "No hay tile entity");
            return ActionResultType.FAIL;
        }


        ItemStack item = player.getItemInHand(handIn);

        if(item.isEmpty()) {
            quitarDisco(player, te);
            return ActionResultType.SUCCESS;
        }

        if(item.getItem() != ItemInit.DISCO.get()) {
            MessageUtil.enviarMensaje(player, "No tieness un disco en la mano");
            return ActionResultType.FAIL;
        }



        CompoundNBT tags = item.getOrCreateTag();

        if (tags.isEmpty()) {
            MessageUtil.enviarMensaje(player, "Este disco está vacío mijo");
            return ActionResultType.FAIL;
        }

        quitarDisco(player, te);
        MessageUtil.enviarMensaje(player, "Has puesto el disco");
        te.setDisco(tags.getString("disco"));
        item.shrink(1);


        return ActionResultType.SUCCESS;

    }

    public void quitarDisco(PlayerEntity player, TocadiscosTE te){
        if(!te.hasDisco()){
            MessageUtil.enviarMensaje(player, "No hay disco");
        }

        String nombre = te.getDisco();
        MessageUtil.enviarMensaje(player, "Has quitado el disco");
        ItemStack disco = new ItemStack(ItemInit.DISCO.get());
        CompoundNBT tags = new CompoundNBT();
        tags.putString("disco", nombre);
        disco.setTag(tags);

        player.addItem(disco);
        te.stopDisco();
        te.setDisco(null);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

}
