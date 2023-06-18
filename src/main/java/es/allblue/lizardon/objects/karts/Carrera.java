package es.allblue.lizardon.objects.karts;

import com.mrcrayfish.vehicle.entity.EngineTier;
import com.mrcrayfish.vehicle.entity.PoweredVehicleEntity;
import com.mrcrayfish.vehicle.entity.vehicle.GoKartEntity;
import com.mrcrayfish.vehicle.init.ModEntities;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.util.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.UUID;

public class Carrera {
    private Circuito circuito;
    private int vueltas;
    private ArrayList<Participante> participantes;
    private EstadoCarrera estado;
    private ArrayList<UUID> votos;

    private ArrayList<UUID> participantesTerminados;

    public Carrera(Circuito circuito, int numeroVueltas) {
        this.circuito = circuito;
        this.vueltas = numeroVueltas;
        this.participantes = new ArrayList<>();
        this.estado = EstadoCarrera.BUSCANDO;
        this.votos = new ArrayList<>();
        this.participantesTerminados = new ArrayList<>();
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

    public void sonido(ServerPlayerEntity player, float pitch) {

        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_HARP, SoundCategory.PLAYERS, 1.0f, pitch);
    }

    public void iniciarCuentaAtras() {
        this.estado = EstadoCarrera.INICIANDO;
        int i = 0;
        for (Participante participante : participantes) {
            // Teleport each participant to a position
            Punto punto = circuito.getInicios().get(i);
            spawnearVehiculo(participante.getJugador(), punto);
            i++;
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                for (int j = 3; j > 0; j--) {
                    for (Participante participante : participantes) {
                        sonido(participante.getJugador(), 0.3f);
                        // Send title to player
                        MessageUtil.enviarTitulo(participante.getJugador(), j + "");
                        participante.getJugador().sendMessage(new StringTextComponent(j + "t"), UUID.randomUUID());
                    }
                    Thread.sleep(1000);
                }
                for (Participante participante : participantes) {
                    MessageUtil.enviarTitulo(participante.getJugador(), "¡YA!");
                    sonido(participante.getJugador(), 0.8f);
                    participante.getJugador().sendMessage(new StringTextComponent("¡YA!"), UUID.randomUUID());
                    moverVehiculo(participante.getJugador().getUUID(), true);
                }
                iniciar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }


    public void spawnearVehiculo(ServerPlayerEntity player, Punto punto) {
        World world = player.level;
        GoKartEntity vehicleEntity = new GoKartEntity(ModEntities.GO_KART.get(), world);
        vehicleEntity.setRequiresFuel(false);
        vehicleEntity.setColorRGB(1, 2, 3);
        vehicleEntity.setEngineTier(EngineTier.WOOD);
        vehicleEntity.setPos(punto.getX() + 0.5f, punto.getY() + 1, punto.getZ() + 0.5f);
        //vehicleEntity.setOwner(uuid);
        vehicleEntity.yRot = -90;


        world.addFreshEntity(vehicleEntity);
        vehicleEntity.getSeatTracker().setSeatIndex(0, player.getUUID());
        player.startRiding(vehicleEntity);
    }

    public static void moverVehiculo(UUID uuid, boolean mover) {
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof PoweredVehicleEntity) {
            PoweredVehicleEntity powered = (PoweredVehicleEntity) vehicle;
            powered.setEngine(mover);
        }
    }

    public void iniciar() {
        this.estado = EstadoCarrera.EN_CURSO;

    }

    public void añadirTerminado(UUID uuid) {
        participantesTerminados.add(uuid);
        if(participantesTerminados.size() == participantes.size()) {
            finalizar();
        }
    }

    private void finalizar() {
        this.estado = EstadoCarrera.FINALIZADA;
        for (Participante participante : participantes) {
            MessageUtil.enviarTitulo(participante.getJugador(), "¡FINALIZADA!");
            sonido(participante.getJugador(), 0.8f);
            participante.getJugador().sendMessage(new StringTextComponent("¡FINALIZADA!"), UUID.randomUUID());

            Lizardon.carreraManager.participantes.remove(participante.getJugador().getUUID());
            printResultados(participante.getJugador());
        }

        Lizardon.carreraManager.carreras.remove(circuito.nombre);
        Lizardon.carreraManager.listarCarreras();
    }

    public void printResultados(ServerPlayerEntity jugador){
        String resultados = "";
        for (Participante participante : participantes) {
            resultados += participante.getJugador().getName() + ": " +MessageUtil.formatearTiempo(participante.getTiempoFin());
        }

        MessageUtil.enviarMensaje(jugador, resultados);

    }
}
