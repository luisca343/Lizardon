package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;

public class AmpliacionJSON {
    String operacion;
    String tipo;
    String user;
    String cantidad;

    String mundo;

    public AmpliacionJSON(String operacion, String tipo, String nombre, String cantidad) {
        this.tipo = tipo;
        this.user = nombre;
        this.operacion = operacion;
        this.cantidad = cantidad;
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

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
