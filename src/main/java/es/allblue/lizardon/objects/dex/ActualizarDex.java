package es.allblue.lizardon.objects.dex;

import es.allblue.lizardon.Lizardon;

public class ActualizarDex {
    private String uuid;

    private String server;
    private int idPokemon;
    private int estado;

    public ActualizarDex(String uuid, int idPokemon, int estado) {
        this.uuid = uuid;
        this.idPokemon = idPokemon;
        this.estado = estado;
        this.server = Lizardon.config.getId();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
