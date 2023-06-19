package es.allblue.lizardon.objects.karts;

import es.allblue.lizardon.util.MessageUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;

public class Participante {
    private ServerPlayerEntity jugador;
    private Carrera carrera;
    private int siguienteCheck;
    private int vuelta;
    private long tiempoFin;

    private boolean terminada;

    private Punto coords;

    public Participante(ServerPlayerEntity jugador, Carrera carrera) {
        this.jugador = jugador;
        this.siguienteCheck = 0;
        this.tiempoFin = 0;
        this.vuelta = 1;
        this.carrera = carrera;
        this.terminada = false;
    }

    public int getSiguienteCheck() {
        return siguienteCheck;
    }

    public void setSiguienteCheck(int siguienteCheck) {
        this.siguienteCheck = siguienteCheck;
    }

    public long getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoFin(long tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public ServerPlayerEntity getJugador() {
        return jugador;
    }

    public void setJugador(ServerPlayerEntity jugador) {
        this.jugador = jugador;
    }

    public int getVuelta() {
        return vuelta;
    }

    public void setVuelta(int vuelta) {
        this.vuelta = vuelta;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public void nuevaVuelta(){
        if (vuelta == carrera.getVueltas()) {
            finCarrera();
            return;
        }

        setSiguienteCheck(1);
        setVuelta(getVuelta() + 1);
        MessageUtil.enviarTitulo(jugador, "Vuelta " + getVuelta() +" / "+carrera.getVueltas());
    }

    public void nuevoCheck(Punto pos){

        if (getSiguienteCheck() == carrera.getCheckpoints().size()) {
            nuevaVuelta();
            return;
        }

        Checkpoint check = carrera.getCheckpoints().get(getSiguienteCheck());
        if(enCheckPoint(pos, check.getPos1(), check.getPos2())){
            setSiguienteCheck(getSiguienteCheck() + 1);
            //MessageUtil.enviarMensaje(jugador, "Has pasado el checkpoint " + getSiguienteCheck());
        }

    }

    public void finCarrera(){
        //carrera.setEnMarcha(false);
        setSiguienteCheck(0);
        setVuelta(0);
        setTiempoFin(System.currentTimeMillis());
        terminada = true;

        jugador.getVehicle().remove();
        MessageUtil.enviarMensaje(jugador, "Has terminado la carrera en " + MessageUtil.formatearTiempo(getTiempoFin() - carrera.getTiempoInicio()));
        carrera.añadirTerminado(jugador.getUUID());

    }


    public void reset(){
        setSiguienteCheck(0);
        setVuelta(1);
        setTiempoFin(0);
    }


    public void tick() {
        if(carrera == null || terminada) return;
        if(carrera.getEstado().equals(EstadoCarrera.EN_CURSO)){
            Punto nuevaPosicion = new Punto(jugador.getX(), jugador.getY(), jugador.getZ());
            if(nuevaPosicion.equals(coords)) return;
            coords = nuevaPosicion;
            nuevoCheck(nuevaPosicion);
        }
    }

    public double getDistanciaSiguienteCheck(){
        Checkpoint check = carrera.getCircuito().getCheckpoints().get(siguienteCheck);
        return jugador.position().distanceTo(new Vector3d(check.getPos1().getX(), check.getPos1().getY(), check.getPos1().getZ()));
    }

    public boolean enCheckPoint(Punto loc, Punto l1, Punto l2){

        double x1 = Math.min(l1.getX(), l2.getX()) -1;
        double y1 = Math.min(l1.getY(), l2.getY()) -1;
        double z1 = Math.min(l1.getZ(), l2.getZ()) -1;

        double x2 = Math.max(l1.getX(), l2.getX()) +1;
        double y2 = Math.min(l1.getY(), l2.getY()) +1;
        double z2 = Math.max(l1.getZ(), l2.getZ()) +1;

        return loc.getX() >= x1 && loc.getX() <= x2
                && loc.getY() >= y1 && loc.getY() <= y2
                && loc.getZ() >= z1 && loc.getZ() <= z2;
    }

}
