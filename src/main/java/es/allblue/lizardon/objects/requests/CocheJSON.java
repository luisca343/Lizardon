package es.allblue.lizardon.objects.requests;

public class CocheJSON {
    String nombre;
    String nbt;

    public CocheJSON(String nombre, String datos) {
        this.nombre = nombre;
        this.nbt = datos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDatos() {
        return nbt;
    }

    public void setDatos(String datos) {
        this.nbt = datos;
    }
}
