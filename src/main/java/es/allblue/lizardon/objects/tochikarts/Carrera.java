package es.allblue.lizardon.objects.tochikarts;

import java.util.ArrayList;
import java.util.UUID;

public class Carrera {
    private Circuito circuito;
    private int vueltas;
    private ArrayList<Participante> participantes;
    private EstadoCarrera estado;
    private ArrayList<UUID> votos;

    public Carrera(Circuito circuito, int numeroVueltas) {
        this.circuito = circuito;
        this.vueltas = numeroVueltas;
        this.participantes = new ArrayList<>();
        this.estado = EstadoCarrera.BUSCANDO;
    }

    public Circuito getCircuito() {
        return circuito;
    }

    public void setCircuito(Circuito circuito) {
        this.circuito = circuito;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public ArrayList<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Participante> participantes) {
        this.participantes = participantes;
    }

    public EstadoCarrera getEstado() {
        return estado;
    }

    public void setEstado(EstadoCarrera estado) {
        this.estado = estado;
    }

    public ArrayList<UUID> getVotos() {
        return votos;
    }

    public void setVotos(ArrayList<UUID> votos) {
        this.votos = votos;
    }

    public void playerTick(Participante participante) {

    }

    public ArrayList<Checkpoint> getCheckpoints() {
        return circuito.getCheckpoints();
    }

    public Checkpoint getCheckpoint(int index) {
        return circuito.getCheckpoint(index);
    }

    public void iniciar(){
        this.estado = EstadoCarrera.EN_CURSO;

    }
}
