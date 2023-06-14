package es.allblue.lizardon.items;

import es.allblue.lizardon.init.ItemBase;
import es.allblue.lizardon.util.music.LizardonSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class LatigoNumeril extends Item {

    public LatigoNumeril(Properties properties) {
        super(properties);
    }


    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        world.playSound(entity, entity.getX(), entity.getY(), entity.getZ(), LizardonSoundEvents.LATIGO_NUMERIL.get(), entity.getSoundSource(), 1.0F, 1.0F);
        return super.use(world, entity, hand);
    }
}
