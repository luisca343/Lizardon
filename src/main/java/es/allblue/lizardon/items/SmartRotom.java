package es.allblue.lizardon.items;


import es.allblue.lizardon.Lizardon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class SmartRotom extends Item {
    public SmartRotom(Properties properties) {
        super(properties);
    }




    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(!stack.hasTag()){
            stack.setTag(new CompoundNBT());
            stack.getTag().putString("PadURL", "http://www.google.es");
            stack.getTag().putInt("PadID", 1);

            Lizardon.PROXY.updatePad(1, stack.getTag(), true);
        }

        if(world.isClientSide()){
            Lizardon.PROXY.openMinePadGui(stack.getTag().getInt("PadID"));

            /*
            player.sendMessage(new StringTextComponent("Patata"), Util.NIL_UUID);
            Lizardon.INSTANCE.abrirSmartRotom();*/
        }
        return super.use(world, player, hand);
    }

    /*
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();

        if(!stack.hasTag()){
            stack.setTag(new CompoundNBT());
            stack.getTag().putString("PadURL", "http://www.google.es");
            stack.getTag().putInt("PadID", 1);

            Lizardon.PROXY.updatePad(1, stack.getTag(), true);
        }

        if(world.isClientSide()){
            Lizardon.PROXY.openMinePadGui(stack.getTag().getInt("PadID"));
            PlayerEntity playerEntity = context.getPlayer();
            BlockState clickedBlock = world.getBlockState(context.getClickedPos());

            playerEntity.sendMessage(new StringTextComponent("Patata"), Util.NIL_UUID);
            Lizardon.INSTANCE.abrirSmartRotom();
            // Lizardon.PROXY.displaySetPadURLGui("http://google.es");
        }
        return super.onItemUseFirst(stack, context);
    }*/


}
