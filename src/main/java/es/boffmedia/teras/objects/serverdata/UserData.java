package es.boffmedia.teras.objects.serverdata;

import es.boffmedia.teras.Teras;
import net.minecraft.entity.player.PlayerEntity;

public class UserData {
    private String uuid;
    private String nombre;
    private String mundo;

    public UserData(String uuid, String nombre) {
        this.uuid = uuid;
        this.nombre = nombre;
        mundo = Teras.PROXY.idServidor;
    }

    public UserData(String uuid, String nombre, String idServer) {
        this.uuid = uuid;
        this.nombre = nombre;
        this.mundo = idServer;
    }

    public UserData(PlayerEntity player) {
        this.uuid = player.getUUID().toString();
        this.nombre = player.getName().getString();
        this.mundo = Teras.config.getId();
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
