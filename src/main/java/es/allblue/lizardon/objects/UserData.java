package es.allblue.lizardon.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import java.util.HashMap;

public class UserData {
    private String uuid;
    private String nombre;
    private String mundo;

    public UserData(String uuid, String nombre) {
        this.uuid = uuid;
        this.nombre = nombre;
        ServerData server = Minecraft.getInstance().getCurrentServer();
        this.mundo = server == null ? "local" : server.ip;
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
