package es.allblue.lizardon.objects.karts;

import com.google.gson.internal.LinkedTreeMap;
import es.allblue.lizardon.util.FileHelper;
import io.leangen.geantyref.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CarreraManager {
    public Map<String, Circuito> circuitos;
    public Map<String, Carrera> carreras;
    public Map<UUID, Participante> participantes;

    public CarreraManager(){
        System.out.println("CarreraManager");
        circuitos = new LinkedTreeMap<>();
        carreras = new HashMap<>();
        participantes = new HashMap<>();

        cargarCircuitos();
    }

    public void cargarCircuitos(){
        Type token = new TypeToken<Map<String, Circuito>>() {}.getType();

       circuitos = (LinkedTreeMap<String, Circuito>) FileHelper.readFile("config/lizardon/circuitos.json", token);

       System.out.println("Circuitos cargados: " + circuitos.size());
        for (Map.Entry<String, Circuito> entry : circuitos.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }


    public void entrarCarrera(String nombreCircuito, ServerPlayerEntity jugador) {
        System.out.println("EntrarCarrera");
        if (!circuitos.containsKey(nombreCircuito)) {
            return;
        }

      if (!carreras.containsKey(nombreCircuito)) {
          Circuito circuito = circuitos.get(nombreCircuito);
           // Crear una nueva carrera
            Carrera carrera = new Carrera(circuito, 3);
            carreras.put(nombreCircuito, carrera);
        }

        Carrera carrera = carreras.get(nombreCircuito);

      System.out.println("PARTICIPANTES:::: ");
      System.out.println(participantes);
      System.out.println(participantes.containsKey(jugador.getUUID()));

        if (participantes.containsKey(jugador.getUUID())) {
            salirCarrera(jugador);
        }


        jugador.sendMessage(new StringTextComponent("Entrando en la carrera"), UUID.randomUUID());
        Participante participante = new Participante(jugador, carrera);
        participantes.put(jugador.getUUID(), participante);
        carrera.getParticipantes().add(participante);

        System.out.println("Participantes: " + carrera.getParticipantes().size());

        for (Participante part : carrera.getParticipantes()) {
            TextComponent mensaje = new StringTextComponent("Participantes: " + carrera.getParticipantes().size());
            StringTextComponent strVotarInicio = new StringTextComponent(TextFormatting.GREEN + "[Votar inicio]");
            Style estilo = strVotarInicio.getStyle()
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/karts votar"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Entrar en carrera")));

            strVotarInicio.setStyle(estilo);

            mensaje.append(strVotarInicio);

            part.getJugador().sendMessage(mensaje, UUID.randomUUID());

        }
    }

    public void salirCarrera(ServerPlayerEntity jugador) {
        jugador.sendMessage(new StringTextComponent("Saliendo de la carrera"), UUID.randomUUID());
        if (!participantes.containsKey(jugador.getUUID())) {
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


    public void votarCircuito(ServerPlayerEntity jugador) {
        if (!participantes.containsKey(jugador.getUUID())) {
            jugador.sendMessage(new StringTextComponent("No estás en ninguna carrera"), UUID.randomUUID());
            return;
        }
        Carrera carrera = participantes.get(jugador.getUUID()).getCarrera();

        if(carrera.getVotos().contains(jugador.getUUID())){
            jugador.sendMessage(new StringTextComponent("Ya has votado por este circuito"), UUID.randomUUID());
            return;
        }
        carrera.getVotos().add(jugador.getUUID());



        if(carrera.getVotos().size() == carrera.getParticipantes().size()){
            carrera.iniciarCuentaAtras();
            carreraBroadcast("Todos los participantes han votado, la carrera comenzará en 10 segundos");

        }

    }

    public void carreraBroadcast(String mensaje){
        for (Map.Entry<UUID, Participante> entry : participantes.entrySet()) {
            Participante participante = entry.getValue();
            participante.getJugador().sendMessage(new StringTextComponent(mensaje), UUID.randomUUID());
        }
    }

    public void votar(ServerPlayerEntity jugador, String nombreCircuito) {
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

    public void playerTick(ServerPlayerEntity jugador) {
        if (!participantes.containsKey(jugador.getUUID())) {
            return;
        }

        Participante participante = participantes.get(jugador.getUUID());
        participante.tick();
    }

    public void iniciarCuentaAtras(String nombre) {
        if (!carreras.containsKey(nombre)) {
            return;
        }

        Carrera carrera = carreras.get(nombre);
        carrera.iniciarCuentaAtras();
    }


    public void iniciarCarrera(String nombre) {
        if (!carreras.containsKey(nombre)) {
            return;
        }

        Carrera carrera = carreras.get(nombre);
        carrera.iniciar();
    }

    public void guardarCircuito(Circuito circuito) {
        circuitos.put(circuito.getNombre(), circuito);
        FileHelper.writeFile("config/lizardon/circuitos.json", circuitos);
    }

    public void listarCircuitos(ServerPlayerEntity player) {
        System.out.println("ListarCircuitos");
        System.out.println(circuitos.size());
        System.out.println(player.getUUID());

        for (Map.Entry<String, Circuito> entry : circuitos.entrySet()) {
           player.sendMessage(new StringTextComponent(entry.getKey()), UUID.randomUUID());
        }

    }

    public void listarCarreras() {
        for (Map.Entry<String, Carrera> entry : carreras.entrySet()){
            System.out.println(entry.getValue());
        }
    }

}
