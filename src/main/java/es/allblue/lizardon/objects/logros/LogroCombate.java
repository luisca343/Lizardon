package es.allblue.lizardon.objects.logros;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import es.allblue.lizardon.objects.pixelmon.PokemonData;

import java.util.ArrayList;
import java.util.List;

public class LogroCombate {
    String uuid;
    String npc;
    boolean victoria;
    ArrayList<PokemonData> equipo;

    public LogroCombate() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNpc() {
        return npc;
    }

    public void setNpc(String npc) {
        this.npc = npc;
    }

    public boolean isVictoria() {
        return victoria;
    }

    public void setVictoria(boolean victoria) {
        this.victoria = victoria;
    }

    public ArrayList<PokemonData> getEquipo() {
        return equipo;
    }

    public void setEquipo(List<Pokemon> equipo) {
        this.equipo = new ArrayList<>();
        for (Pokemon pokemon : equipo) {
            PokemonData pokemonData = new PokemonData(pokemon);
            this.equipo.add(pokemonData);
        }
    }
}
