package es.allblue.lizardon.objects.logros;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

import java.util.List;

public class LogroCombate {
    String uuid;
    String npc;
    boolean victoria;
    String equipo;

    public LogroCombate(String uuid, String npc, boolean resultado, String equipo) {
        this.uuid = uuid;
        this.npc = npc;
        this.victoria = resultado;
        this.equipo = equipo;
    }

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

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(List<Pokemon> equipo) {

    }
}
