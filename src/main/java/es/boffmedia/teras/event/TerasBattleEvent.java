package es.boffmedia.teras.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.objects.TrainerDefeatMoney;
import es.boffmedia.teras.objects_old.logros.LogroCombate;
import es.boffmedia.teras.pixelmon.battle.TerasBattle;
import es.boffmedia.teras.pixelmon.battle.CombateFrenteBatalla;
import es.boffmedia.teras.pixelmon.frentebatalla.TorreBatallaController;
import es.boffmedia.teras.util.FileHelper;
import es.boffmedia.teras.util.MessageHelper;
import es.boffmedia.teras.util.Scoreboard;
import es.boffmedia.teras.util.WingullAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class TerasBattleEvent {

    @SubscribeEvent
    public void beatTrainer(BeatTrainerEvent event){
        if(event.trainer.getWinMoney() == 0){
            Teras.LOGGER.info("No hay dinero que ganar, esto podría ser un combate custom, verdad Luisca??");
            return;
        }
        
        TrainerDefeatMoney defeatMoney = new TrainerDefeatMoney(event.player.getStringUUID(), event.trainer.getWinMoney());

        Gson gson = new Gson();
        WingullAPI.wingullPOST("/starbank/trainerdefeat", gson.toJson(defeatMoney));
    }

    public void inicioCombateEntrenador(BattleStartedEvent event, TerasBattle combate){
        Teras.LOGGER.info("Iniciando combate entrenador");
        //TerasBattleLog.parseLog(event.bc.battleLog, combate);
    }

    public void inicioCombateSalvaje(BattleStartedEvent event, TerasBattle combate){
        Teras.LOGGER.info("Iniciando combate salvaje");
    }

    public void finCombateEntrenador(BattleEndEvent event, TerasBattle combate){
        Teras.LOGGER.info("Fin combate entrenador");
        //TerasBattleLog.parseLog(event.getBattleController().battleLog, combate);
        boolean ganador = getGanador(event, combate);

        LogroCombate logroCombate = new LogroCombate();
        logroCombate.setUuid(combate.getPlayer().getStringUUID());
        logroCombate.setNpc(combate.getBattleConfig().getNombreArchivo());
        logroCombate.setNpc(combate.getBattleConfig().getNombreArchivo());
        logroCombate.setLogro(combate.getBattleConfig().getLogro());
        logroCombate.setVictoria(ganador);
        List<Pokemon> team = StorageProxy.getParty(combate.getPlayer()).getTeam();
        logroCombate.setEquipo(team);

        WingullAPI.wingullPOST("/logros/combate", Teras.GSON.toJson(logroCombate));

        FileHelper.writeStringFile("logs/terasbattle/"+combate.getBattleConfig().getNombreArchivo()+".log", combate.getLog());
    }

    public void finCombateSalvaje(BattleEndEvent event, TerasBattle combate){
        getGanador(event, combate);
    }

    public boolean getGanador(BattleEndEvent event, TerasBattle combate){
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
            String nombreObjetivo = combate.getBattleConfig().getNombreObjetivo();

            Scoreboard.set(combate.getPlayer(), nombreObjetivo, 1);
            MessageHelper.enviarMensaje(combate.getPlayer(), TextFormatting.GREEN + "Has ganado el combate contra " + combate.getBattleConfig().getNombre());
            MessageHelper.enviarMensaje(combate.getPlayer(), TextFormatting.GREEN + "Obtienes " + combate.getBattleConfig().getDinero() + " Pokedolares");
            return true;
        }else {
            MessageHelper.enviarMensaje(combate.getPlayer(), TextFormatting.RED + "Has perdido el combate contra " + combate.getBattleConfig().getNombre());
            return false;
        }

    }

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        if(Teras.getLBC().existsTerasBattle(event.getBattleController().battleIndex)) {
            TerasBattle combate = Teras.getLBC().getTerasBattle(event.getBattleController().battleIndex);
            if(combate.getBattleConfig().esEntrenador()) inicioCombateEntrenador(event, combate);
            else inicioCombateSalvaje(event, combate);
        }
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event){
        if(Teras.getLBC().existsTerasBattle(event.getBattleController().battleIndex)) {
            TerasBattle combate = Teras.getLBC().getTerasBattle(event.getBattleController().battleIndex);

            if(combate.getRivalParticipant().getEntity() != null) combate.getRivalParticipant().getEntity().remove();


            if( combate.getBattleConfig().esEntrenador()) {
                //TerasBattleLog.parseLog(event.getBattleController().battleLog, combate);

                if(combate instanceof CombateFrenteBatalla) finCombateFrenteBatalla(event, combate);
                else finCombateEntrenador(event, combate);
            }
            else finCombateSalvaje(event, combate);

            MobEntity entity = Teras.getLBC().getTerasBattle(event.getBattleController().battleIndex).getEntity();
            if(entity != null){
                entity.remove();
            }
            Teras.getLBC().removeTerasBattle(event.getBattleController().battleIndex);
        }
    }

    private void finCombateFrenteBatalla(BattleEndEvent event, TerasBattle combate) {
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
                MessageHelper.enviarMensaje(p, "§cHas perdido el combate contra " + combateFrenteBatalla.getBattleConfig().getNombre());
            });
        }
    }
}
