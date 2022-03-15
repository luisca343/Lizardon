package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;

public class GimnasioJSON {
    String tipo;
    String user;

    String mundo;

    public GimnasioJSON(String tipo, String nombre) {
        this.tipo = tipo;
        this.user = nombre;
        mundo = Lizardon.NOMBRE_MUNDO;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return user;
    }

    public void setNombre(String nombre) {
        this.user = nombre;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMundo() {
        return mundo;
    }

    public void setMundo(String mundo) {
        this.mundo = mundo;
    }
}
