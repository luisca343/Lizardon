package es.allblue.lizardon.event.karts;

import es.allblue.lizardon.Lizardon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "lizardon")
public class CarreraEvent {

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if(!event.player.level.isClientSide()){
                ServerPlayerEntity player = (ServerPlayerEntity) event.player;
                Lizardon.carreraManager.playerTick(player);
            }
        }
    }
}
