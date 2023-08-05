package es.allblue.lizardon.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.battles.BattleEndCause;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import es.allblue.lizardon.commands.CombateCommand;
import es.allblue.lizardon.objects.Entrenador;
import es.allblue.lizardon.objects.dex.ActualizarDex;
import es.allblue.lizardon.util.Reader;
import es.allblue.lizardon.util.Scoreboard;
import es.allblue.lizardon.util.WingullAPI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class LizardonBattleEvent {

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void guardarDex(PokedexEvent.Post event){
        String uuid = event.getPlayer().getStringUUID();
        int idPokemon = event.getPokemon().getSpecies().getDex();
        int estado = event.getNewStatus().equals(PokedexRegistrationStatus.SEEN) ? 0 : 1;

        ActualizarDex dex = new ActualizarDex(uuid, idPokemon, estado);
        Gson gson = new Gson();

        WingullAPI.wingullPOST("rotom/dex", gson.toJson(dex));
    }

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        BattleController bc = event.bc;
        List<BattleParticipant> participants = event.bc.participants;


        if(participants.size() == 2){
            BattleParticipant participant1 = participants.get(0);
            BattleParticipant participant2 = participants.get(1);

            ServerPlayerEntity player = null;
            if(participant1 instanceof PlayerParticipant){
                player = ((PlayerParticipant) participant1).player;
            }


            if(participant2 instanceof TrainerParticipant){
                TrainerParticipant npc = (TrainerParticipant) participant2;
                Entrenador e = Reader.getDatosNPC(npc.trainer.greeting);
                if(e == null) return;

                if(e.curar()){
                    StorageProxy.getParty(player).heal();
                }


                player.sendMessage(new StringTextComponent("Iniciando combate contra " + npc.trainer.greeting), UUID.randomUUID());
            }
        }

    }

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

                Entrenador e = Reader.getDatosNPC(npc.trainer.greeting);
                e.recibirRecompensas(player.getUUID());

                CombateCommand.combatesActivos.get(player.getUUID()).getNpc().say("HASS GANDAO FELICIDADES LUISCA");
                CombateCommand.combatesActivos.remove(player.getUUID());

                Scoreboard.set(player, npc.trainer.greeting, 1);
            }
        }
        System.out.println("Battle ended");
    }
}
