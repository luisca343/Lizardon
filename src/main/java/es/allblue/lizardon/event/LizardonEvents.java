package es.allblue.lizardon.event;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(Dist.CLIENT)
public class LizardonEvents {
    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock e) {
        {

        }
    }
}
