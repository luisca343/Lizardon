package es.allblue.lizardon.objects;

import es.allblue.lizardon.Lizardon;
import net.minecraft.util.ResourceLocation;


public class Contacto {
    private String uuid;
    private String nombre;
    private ResourceLocation icono;

    public Contacto(String uuid, String nombre) {
        this.uuid = uuid;
        this.nombre = nombre;
        this.icono = new ResourceLocation(Lizardon.MODID, "textures/user.jpg");
    }

    public Contacto(String uuid, String nombre, ResourceLocation icono) {
        this.uuid = uuid;
        this.nombre = nombre;
        this.icono = icono;
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

    public void setIcono(ResourceLocation icono) {
        this.icono = icono;
    }

    public ResourceLocation getIcono() {
        return this.icono;
    }
}
