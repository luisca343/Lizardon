package es.allblue.lizardon.objects.serverdata;

import es.allblue.lizardon.Lizardon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class UserData {
    private String uuid;
    private String nombre;
    private String mundo;

    public UserData(String uuid, String nombre) {
        this.uuid = uuid;
        this.nombre = nombre;
        mundo = Lizardon.PROXY.idServidor;
    }

    public UserData(String uuid, String nombre, String idServer) {
        this.uuid = uuid;
        this.nombre = nombre;
        this.mundo = idServer;
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
