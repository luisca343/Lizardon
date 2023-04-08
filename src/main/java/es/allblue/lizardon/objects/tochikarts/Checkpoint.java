package es.allblue.lizardon.objects.tochikarts;

public class Checkpoint {
    private Punto inicio, fin;

    public Checkpoint(Punto inicio, Punto fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public Punto getInicio() {
        return inicio;
    }

    public void setInicio(Punto inicio) {
        this.inicio = inicio;
    }

    public Punto getFin() {
        return fin;
    }

    public void setFin(Punto fin) {
        this.fin = fin;
    }

    @Override
    public String toString() {
        return "["+inicio.toString()+" "+fin.toString()+"]";
    }
}
