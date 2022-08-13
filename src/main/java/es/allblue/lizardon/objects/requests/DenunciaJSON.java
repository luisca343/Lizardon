package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;

public class DenunciaJSON {
    String user;
    String razon;

    String mundo;

    public DenunciaJSON(String nombre, String razon) {
        this.user = nombre;
        this.razon = razon;
        mundo = Lizardon.NOMBRE_MUNDO;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getMundo() {
        return mundo;
    }

    public void setMundo(String mundo) {
        this.mundo = mundo;
    }
}
