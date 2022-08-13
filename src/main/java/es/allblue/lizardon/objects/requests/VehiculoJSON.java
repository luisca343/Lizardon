package es.allblue.lizardon.objects.requests;

public class VehiculoJSON {
    String nombre;
    String nbt;

    public VehiculoJSON(String nombre, String nbt) {
        this.nombre = nombre;
        this.nbt = nbt;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNbt() {
        return nbt;
    }

    public void setNbt(String nbt) {
        this.nbt = nbt;
    }
}
