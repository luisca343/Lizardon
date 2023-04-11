package es.allblue.lizardon.objects.tochikarts;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CarreraManager {
    public static HashMap<String, Circuito> circuitos = new HashMap<>();
    public static HashMap<String, Carrera> carreras = new HashMap<>();
    public static HashMap<UUID, Participante> participantes = new HashMap<>();

    public static void entrarCarrera(String nombreCircuito, ServerPlayerEntity jugador) {
        if (!carreras.containsKey(nombreCircuito)) {
           // Crear una nueva carrera
            jugador.sendMessage(new StringTextComponent("Creando carrera"), UUID.randomUUID());
            Carrera carrera = new Carrera(circuitos.get(nombreCircuito), 3);
            carreras.put(nombreCircuito, carrera);
        }

        Carrera carrera = carreras.get(nombreCircuito);
        if (participantes.containsKey(jugador)) {
            jugador.sendMessage(new StringTextComponent("Ya estás en una carrera"), UUID.randomUUID());
            return;
        }

        jugador.sendMessage(new StringTextComponent("Entrando en la carrera"), UUID.randomUUID());
        participantes.put(jugador.getUUID(), new Participante(jugador, carrera));
        carrera.getParticipantes().add(new Participante(jugador, carrera));
    }

    public static void salirCarrera(ServerPlayerEntity jugador) {
        if (!participantes.containsKey(jugador)) {
            jugador.sendMessage(new StringTextComponent("No estás en ninguna carrera"), UUID.randomUUID());
            return;
        }

        Carrera carrera = participantes.get(jugador.getUUID()).getCarrera();
        carrera.getParticipantes().removeIf(participante -> participante.getJugador().getUUID().equals(jugador.getUUID()));
        participantes.remove(jugador);
        jugador.sendMessage(new StringTextComponent("Has salido de la carrera"), UUID.randomUUID());

        if (carrera.getParticipantes().size() == 0) {
            carreras.remove(carrera.getCircuito().getNombre());
            jugador.sendMessage(new StringTextComponent("La carrera ha sido eliminada al no tener participantes"), UUID.randomUUID());

        }
    }

    public static void votar(ServerPlayerEntity jugador, String nombreCircuito) {
        if (!carreras.containsKey(nombreCircuito)) {
            jugador.sendMessage(new StringTextComponent("No existe ninguna carrera con ese nombre"), UUID.randomUUID());
            return;
        }

        Carrera carrera = carreras.get(nombreCircuito);
        if (carrera.getVotos().contains(jugador.getUUID())) {
            jugador.sendMessage(new StringTextComponent("Ya has votado por esta carrera"), UUID.randomUUID());
            return;
        }

        carrera.getVotos().add(jugador.getUUID());
        jugador.sendMessage(new StringTextComponent("Has votado por esta carrera"), UUID.randomUUID());

    }

    public static void playerTick(ServerPlayerEntity jugador) {
        if (!participantes.containsKey(jugador.getUUID())) {
            return;
        }

        Participante participante = participantes.get(jugador.getUUID());
        participante.tick();
    }

    public static void iniciarCarrera(String nombre) {
        if (!carreras.containsKey(nombre)) {
            return;
        }

        Carrera carrera = carreras.get(nombre);
        carrera.iniciar();
    }
}
