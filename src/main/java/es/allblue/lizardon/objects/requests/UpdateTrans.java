package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;

public class UpdateTrans {
    int id;
    String nombre;
    int balance;

    String mundo;

    public UpdateTrans(int id, String nombre, int balance) {
        this.id = id;
        this.nombre = nombre;
        this.balance = balance;
        mundo = Lizardon.NOMBRE_MUNDO;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMundo() {
        return mundo;
    }

    public void setMundo(String mundo) {
        this.mundo = mundo;
    }
}
