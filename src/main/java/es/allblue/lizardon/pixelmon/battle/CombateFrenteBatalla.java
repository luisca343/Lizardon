package es.allblue.lizardon.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.pixelmon.battle.Combate;
import es.allblue.lizardon.objects.pixelmon.ConfigCombate;
import es.allblue.lizardon.util.FileHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public class CombateFrenteBatalla extends Combate {
    public CombateFrenteBatalla(ServerPlayerEntity player, ConfigCombate configCombate) {
        super(player, configCombate);
    }

    @Override
    public PlayerParticipant getPartJugador(){
        List<Pokemon> pokemon = TeamManager.getTeam(player, "equipo");

        for (Pokemon pokemon1 : pokemon) {
            pokemon1.setLevel(50);
        }
        return new PlayerParticipant(player, pokemon, configCombate.numPokemonJugador());
    }

}
