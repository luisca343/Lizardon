package es.allblue.lizardon.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import es.allblue.lizardon.commands.CombateCommand;
import es.allblue.lizardon.objects.dex.ActualizarDex;
import es.allblue.lizardon.objects.pixelmon.Combate;
import es.allblue.lizardon.util.MessageUtil;
import es.allblue.lizardon.util.WingullAPI;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

    public void inicioCombateEntrenador(BattleStartedEvent event, Combate combate){
        MessageUtil.enviarMensaje(combate.getPlayer(), TextFormatting.LIGHT_PURPLE + "¡Recibidos datos de combate!");
        MessageUtil.enviarMensaje(combate.getPlayer(), TextFormatting.LIGHT_PURPLE + combate.getConfigCombate().toString());
        MessageUtil.enviarMensaje(combate.getPlayer(), TextFormatting.LIGHT_PURPLE + combate.getPartRival().allPokemon.toString());
    }

    public void inicioCombateSalvaje(BattleStartedEvent event, Combate combate){

    }

    public void finCombateEntrenador(BattleEndEvent event, Combate combate){

    }

    public void finCombateSalvaje(BattleEndEvent event, Combate combate){

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

            CombateCommand.combatesEspeciales.remove(event.getBattleController().battleIndex);
        }

    }
}
