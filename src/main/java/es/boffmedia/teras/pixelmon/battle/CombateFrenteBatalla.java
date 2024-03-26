package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import es.boffmedia.teras.objects.pixelmon.ConfigCombate;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class CombateFrenteBatalla extends Combate {
    private String modalidad;
    public CombateFrenteBatalla(ServerPlayerEntity player, ConfigCombate configCombate) {
        super(player, configCombate);
    }

    @Override
    public PlayerParticipant getPartJugador(){
        List<Pokemon> pokemon = getPlayerParty().findAll(Pokemon::canBattle);
        for (Pokemon pokemon1 : pokemon) {
            pokemon1.setLevel(50);
        }
        return new PlayerParticipant(player, pokemon, configCombate.getNumPkmJugador());
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

}
