package es.allblue.lizardon.objects.requests;

public class DesbloquearAppJSON {
    String app;
    String nombre;
    String mundo;

    public DesbloquearAppJSON(String app, String nombre, String mundo) {
        this.app = app;
        this.nombre = nombre;
        this.mundo = mundo;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
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


