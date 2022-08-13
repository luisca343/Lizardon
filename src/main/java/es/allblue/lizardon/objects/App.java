package es.allblue.lizardon.objects;

import es.allblue.lizardon.gui.Pantalla;
import net.minecraft.util.ResourceLocation;

public class App {

    String nombre;
    ResourceLocation icono;
    Pantalla pantalla;

    public App(String nombre, ResourceLocation icono, Pantalla pantalla) {
        this.nombre = nombre;
        this.icono = icono;
        this.pantalla = pantalla;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ResourceLocation getIcono() {
        return icono;
    }

    public void setIcono(ResourceLocation icono) {
        this.icono = icono;
    }

    public Pantalla getPantalla() {
        return pantalla;
    }

    public void setPantalla(Pantalla pantalla) {
        this.pantalla = pantalla;
    }
}
