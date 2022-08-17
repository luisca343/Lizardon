package es.allblue.lizardon.objects;

import java.util.HashMap;

public class UserData {
    private String uuid;
    private String nombre;

    public UserData(String uuid, String nombre) {
        this.uuid = uuid;
        this.nombre = nombre;
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
}
