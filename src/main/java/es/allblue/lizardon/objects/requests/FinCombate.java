package es.allblue.lizardon.objects.requests;

public class FinCombate {
    String ganador;
    String perdedor;

    public FinCombate(String ganador, String perdedor) {
        this.ganador = ganador;
        this.perdedor = perdedor;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public String getPerdedor() {
        return perdedor;
    }

    public void setPerdedor(String perdedor) {
        this.perdedor = perdedor;
    }
}
