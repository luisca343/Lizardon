package es.allblue.lizardon.objects;

public class EventoDex {
    String mundo;
    String uuid;
    int idPokemon;
    String estado;

    public EventoDex(String mundo, String uuid, int idPokemon, String estado) {
        this.mundo = mundo;
        this.uuid = uuid;
        this.idPokemon = idPokemon;
        this.estado = estado;
    }

    public String getMundo() {
        return mundo;
    }

    public void setMundo(String mundo) {
        this.mundo = mundo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
