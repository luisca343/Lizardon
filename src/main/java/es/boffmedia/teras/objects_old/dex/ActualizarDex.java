package es.boffmedia.teras.objects_old.dex;

import es.boffmedia.teras.Teras;

public class ActualizarDex {
    private String uuid;
    private String server;
    private int pokemonId;
    String form;
    String palette;
    private int status;

    public ActualizarDex(String uuid, int idPokemon, int estado, String form, String palette) {
        this.uuid = uuid;
        this.pokemonId = idPokemon;
        this.status = estado;
        this.server = Teras.config.getId();
        this.form = form;
        this.palette = palette;
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
        return pokemonId;
    }

    public void setIdPokemon(int idPokemon) {
        this.pokemonId = idPokemon;
    }

    public int getEstado() {
        return status;
    }

    public void setEstado(int estado) {
        this.status = estado;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getPalette() {
        return palette;
    }

    public void setPalette(String palette) {
        this.palette = palette;
    }
}
