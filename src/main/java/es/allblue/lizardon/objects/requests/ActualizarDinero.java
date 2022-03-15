package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;

public class ActualizarDinero {
    String usuario;
    int dinero;

    String mundo;

    public ActualizarDinero(String usuario, int dinero) {
        this.usuario = usuario;
        this.dinero = dinero;

        mundo = Lizardon.NOMBRE_MUNDO;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public String getMundo() {
        return mundo;
    }

    public void setMundo(String mundo) {
        this.mundo = mundo;
    }
}
