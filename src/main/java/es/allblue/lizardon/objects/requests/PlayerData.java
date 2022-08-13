package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;

public class PlayerData {
    String uuid;
    String nombre;

    String mundo;

    public PlayerData(String uuid, String nombre) {
        this.uuid = uuid;
        this.nombre = nombre;


        mundo = Lizardon.NOMBRE_MUNDO;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMundo() {
        return mundo;
    }

    public void setMundo(String mundo) {
        this.mundo = mundo;
    }
}
