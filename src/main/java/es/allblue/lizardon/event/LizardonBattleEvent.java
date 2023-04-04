package es.allblue.lizardon.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.battles.BattleEndCause;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import es.allblue.lizardon.objects.Entrenador;
import es.allblue.lizardon.util.Reader;
import es.allblue.lizardon.util.Scoreboard;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class LizardonBattleEvent {
    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event){
        if(event.getCause().equals(BattleEndCause.FORCE)) return;
        BattleController bc = event.getBattleController();
        List<BattleParticipant> participants = event.getBattleController().participants;
        if(participants.size() == 2){
            BattleParticipant participant1 = participants.get(0);
            BattleParticipant participant2 = participants.get(1);

            ServerPlayerEntity player = null;
            if(participant1 instanceof PlayerParticipant){
                player = ((PlayerParticipant) participant1).player;
            }

            if(participant2 instanceof TrainerParticipant){
                BattleResults result = event.getResult(player).get();

                if(!result.equals(BattleResults.VICTORY)){
                    player.sendMessage(new StringTextComponent("No has ganado pringao jajaja jajaja "), UUID.randomUUID());
                    return;
                }
                TrainerParticipant npc = (TrainerParticipant) participant2;
                String nombreNPC = npc.trainer.greeting;
                Gson gson = new Gson();
                Entrenador e = gson.fromJson(Reader.getDatosNPC(nombreNPC), Entrenador.class);
                e.recibirRecompensas(player.getUUID());

                Scoreboard.set(player, npc.trainer.greeting, 1);
            }
        }
        System.out.println("Battle ended");

    }
}
