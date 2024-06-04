package es.boffmedia.teras.objects_old.logros;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import es.boffmedia.teras.objects.post.SmartRotomPost;
import es.boffmedia.teras.objects_old.pixelmon.PokemonData;

import java.util.ArrayList;
import java.util.List;

public class LogroCombate extends SmartRotomPost {
    String npc;
    boolean victoria;
    String logro;
    ArrayList<PokemonData> equipo;
    String replay;

    public LogroCombate() {

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

    public String getLogro() {
        return logro;
    }

    public void setLogro(String logro) {
        this.logro = logro;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }
}
