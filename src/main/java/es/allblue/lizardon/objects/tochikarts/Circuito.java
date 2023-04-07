package es.allblue.lizardon.objects.tochikarts;

import java.util.ArrayList;
import java.util.LinkedList;

public class Circuito {
    String nombre;
    ArrayList<Checkpoint> checkpoints;
    LinkedList<Punto> inicios;

    public Circuito() {}

    public Circuito(String nombre) {
        this.nombre = nombre;
        this.checkpoints = new ArrayList<>();
        this.inicios = new LinkedList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public LinkedList<Punto> getInicios() {
        return inicios;
    }

    public void nuevoInicio(Punto inicio) {
        inicios.addLast(inicio);
    }
}
