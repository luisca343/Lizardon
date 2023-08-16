package es.allblue.lizardon.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import es.allblue.lizardon.commands.CombateCommand;
import es.allblue.lizardon.objects.dex.ActualizarDex;
import es.allblue.lizardon.objects.pixelmon.Combate;
import es.allblue.lizardon.util.MessageUtil;
import es.allblue.lizardon.util.Scoreboard;
import es.allblue.lizardon.util.WingullAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noppes.npcs.api.entity.ICustomNpc;
import org.h2.tools.Server;

import java.util.Map;

@Mod.EventBusSubscriber
public class LizardonBattleEvent {


    public void inicioCombateEntrenador(BattleStartedEvent event, Combate combate){

    }

    public void inicioCombateSalvaje(BattleStartedEvent event, Combate combate){

    }

    public void finCombateEntrenador(BattleEndEvent event, Combate combate){
        getGanador(event, combate);
    }

    public void finCombateSalvaje(BattleEndEvent event, Combate combate){
        getGanador(event, combate);
    }

    public void getGanador(BattleEndEvent event, Combate combate){
        LivingEntity ganador = null, perdedor = null;
        try{
            for(Map.Entry<BattleParticipant, BattleResults> entry : event.getResults().entrySet()){
                if(entry.getValue() == BattleResults.VICTORY){
                    ganador = entry.getKey().getEntity();
                }else if(entry.getValue() == BattleResults.DEFEAT){
                    perdedor = entry.getKey().getEntity();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        if(ganador instanceof ServerPlayerEntity) {
            String nombreObjetivo = combate.getConfigCombate().getNombreObjetivo();

            Scoreboard.set(combate.getPlayer(), nombreObjetivo, 1);
            MessageUtil.enviarMensaje(combate.getPlayer(), TextFormatting.GREEN + "Has ganado el combate contra " + combate.getConfigCombate().getNombre());
        }else {
            MessageUtil.enviarMensaje(combate.getPlayer(), TextFormatting.RED + "Has perdido el combate contra " + combate.getConfigCombate().getNombre());
        }
    }

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        if(CombateCommand.combatesEspeciales.containsKey(event.bc.battleIndex)) {
            Combate combate = CombateCommand.combatesEspeciales.get(event.bc.battleIndex);
            if(combate.getConfigCombate().esEntrenador()) inicioCombateEntrenador(event, combate);
            else inicioCombateSalvaje(event, combate);
        }


    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event){
        if(CombateCommand.combatesEspeciales.containsKey(event.getBattleController().battleIndex)) {
            Combate combate = CombateCommand.combatesEspeciales.get(event.getBattleController().battleIndex);
            if(combate.getConfigCombate().esEntrenador()) finCombateEntrenador(event, combate);
            else finCombateSalvaje(event, combate);

            MobEntity entity = CombateCommand.combatesEspeciales.get(event.getBattleController().battleIndex).getEntidad();
            if(entity != null){
                entity.remove();
            }
            CombateCommand.combatesEspeciales.remove(event.getBattleController().battleIndex);
        }

    }
}
