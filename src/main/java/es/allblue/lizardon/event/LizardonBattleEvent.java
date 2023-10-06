package es.allblue.lizardon.event;

import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleLog;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.logros.LogroCombate;
import es.allblue.lizardon.pixelmon.battle.Combate;
import es.allblue.lizardon.pixelmon.battle.CombateFrenteBatalla;
import es.allblue.lizardon.pixelmon.frentebatalla.TorreBatallaController;
import es.allblue.lizardon.util.FileHelper;
import es.allblue.lizardon.util.MessageHelper;
import es.allblue.lizardon.util.Scoreboard;
import es.allblue.lizardon.util.WingullAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class LizardonBattleEvent {


    public void inicioCombateEntrenador(BattleStartedEvent event, Combate combate){
        Lizardon.LOGGER.info("Iniciando combate entrenador");
        //LizardonBattleLog.parseLog(event.bc.battleLog, combate);
    }

    public void inicioCombateSalvaje(BattleStartedEvent event, Combate combate){
        Lizardon.LOGGER.info("Iniciando combate salvaje");
    }

    public void finCombateEntrenador(BattleEndEvent event, Combate combate){
        Lizardon.LOGGER.info("Fin combate entrenador");
        //LizardonBattleLog.parseLog(event.getBattleController().battleLog, combate);
        boolean ganador = getGanador(event, combate);

        LogroCombate logroCombate = new LogroCombate();
        logroCombate.setUuid(combate.getPlayer().getStringUUID());
        logroCombate.setNpc(combate.getConfigCombate().getNombreArchivo());
        logroCombate.setNpc(combate.getConfigCombate().getNombreArchivo());
        logroCombate.setLogro(combate.getConfigCombate().getLogro());
        logroCombate.setVictoria(ganador);
        List<Pokemon> team = StorageProxy.getParty(combate.getPlayer()).getTeam();
        logroCombate.setEquipo(team);

        WingullAPI.wingullPOST("/logros/combate", Lizardon.GSON.toJson(logroCombate));

        FileHelper.writeStringFile("logs/lizardonbattle/"+combate.getConfigCombate().getNombreArchivo()+".log", combate.getLog());
    }

    public void finCombateSalvaje(BattleEndEvent event, Combate combate){
        getGanador(event, combate);
    }

    public boolean getGanador(BattleEndEvent event, Combate combate){
        LivingEntity ganador = null, perdedor = null;
        try{
            for(Map.Entry<BattleParticipant, BattleResults> entry : event.getResults().entrySet()){
                if(entry.getValue() == BattleResults.VICTORY){
                    ganador = entry.getKey().getEntity();
                } else if(entry.getValue() == BattleResults.DEFEAT){
                    perdedor = entry.getKey().getEntity();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(ganador instanceof ServerPlayerEntity) {
            String nombreObjetivo = combate.getConfigCombate().getNombreObjetivo();

            Scoreboard.set(combate.getPlayer(), nombreObjetivo, 1);
            MessageHelper.enviarMensaje(combate.getPlayer(), TextFormatting.GREEN + "Has ganado el combate contra " + combate.getConfigCombate().getNombre());
            MessageHelper.enviarMensaje(combate.getPlayer(), TextFormatting.GREEN + "Obtienes " + combate.getConfigCombate().getDinero() + " Pokedolares");
            return true;
        }else {
            MessageHelper.enviarMensaje(combate.getPlayer(), TextFormatting.RED + "Has perdido el combate contra " + combate.getConfigCombate().getNombre());
            return false;
        }

    }

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        if(Lizardon.getLBC().existeCombateEspecial(event.getBattleController().battleIndex)) {
            Combate combate = Lizardon.getLBC().getCombateEspecial(event.getBattleController().battleIndex);
            if(combate.getConfigCombate().esEntrenador()) inicioCombateEntrenador(event, combate);
            else inicioCombateSalvaje(event, combate);
        }


    }

    public void log(BattleLog log){

    }


    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event){
        log(event.getBattleController().battleLog);
        if(Lizardon.getLBC().existeCombateEspecial(event.getBattleController().battleIndex)) {
            Combate combate = Lizardon.getLBC().getCombateEspecial(event.getBattleController().battleIndex);

            if(combate.getPartRival().getEntity() != null) combate.getPartRival().getEntity().remove();


            if( combate.getConfigCombate().esEntrenador()) {
                if(combate instanceof CombateFrenteBatalla) finCombateFrenteBatalla(event, combate);
                else finCombateEntrenador(event, combate);
            }
            else finCombateSalvaje(event, combate);

            MobEntity entity = Lizardon.getLBC().getCombateEspecial(event.getBattleController().battleIndex).getEntidad();
            if(entity != null){
                entity.remove();
            }
            Lizardon.getLBC().removeCombateEspecial(event.getBattleController().battleIndex);
        }
    }

    private void finCombateFrenteBatalla(BattleEndEvent event, Combate combate) {
        CombateFrenteBatalla combateFrenteBatalla = (CombateFrenteBatalla) combate;
        if(getGanador(event, combateFrenteBatalla)){
            // Send message to all players
            event.getPlayers().forEach(p -> {
                String modalidad = combateFrenteBatalla.getModalidad();
                int victorias = p.getPersistentData().getInt(modalidad) + 1;
                p.getPersistentData().putInt(modalidad, victorias);
                TorreBatallaController.eleccionesCombate(p);
            });
           } else {
            event.getPlayers().forEach(p -> {
                MessageHelper.enviarMensaje(p, "§cHas perdido el combate contra " + combateFrenteBatalla.getConfigCombate().getNombre());
            });
        }
    }
}
