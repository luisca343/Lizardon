package es.boffmedia.teras.event.karts;

import es.boffmedia.teras.Teras;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "teras")
public class CarreraEvent {

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player.level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            Teras.carreraManager.playerTick(player);
        }
    }
}
