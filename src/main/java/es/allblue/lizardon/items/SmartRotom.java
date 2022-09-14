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
            int id = Lizardon.PROXY.getNextPadID();
            stack.getTag().putInt("PadID", id);

            Lizardon.PROXY.updatePad(id, stack.getTag(), true);
        }

        if(world.isClientSide()){
            Lizardon.PROXY.openMinePadGui(stack.getTag().getInt("PadID"));
        }
        return super.use(world, player, hand);
    }
}
