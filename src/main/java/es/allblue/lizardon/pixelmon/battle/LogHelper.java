package es.allblue.lizardon.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import es.allblue.lizardon.objects.pixelmon.PosicionEquipo;

import java.util.HashMap;

public class LogHelper {
    public LogHelper() { }
    
    public void enviarMensajeCambio(PosicionEquipo pos, PixelmonWrapper pk, Combate combate) {
        combate.appendLog("|switch|" + pos.toString() + ": " + getDatosPoke(pk, false) + System.lineSeparator());
    }

    public String getDatosPoke(PixelmonWrapper pk, boolean first){
        String gender = pk.getGender().equals(Gender.FEMALE) ? ", F" :  (pk.getGender().equals(Gender.MALE) ? ", M" : "");
        String shiny = pk.getRealTextureNoCheck().contains("shiny") ? ", shiny" : "";
        String nickname = pk.getNickname();

        String nombre = pk.getSpecies().getName();
        int health = first ? pk.getMaxHealth() : pk.getHealth();

        return nickname + "|" + nombre + gender + shiny + "|" + health + "\\/" + pk.getMaxHealth();
    }

    public int getPokemonRestantes(PosicionEquipo posObjetivo, Combate combate) {
        // get team
        return posObjetivo.getEquipo() == 1 ? combate.getPartJugador().countAblePokemon() : combate.getPartRival().countAblePokemon();
    }

    public int getPlayerContrario(PosicionEquipo posicion) {
        int equipo = posicion.getEquipo();
        if(equipo == 1) return 2;
        else return 1;
    }

}
