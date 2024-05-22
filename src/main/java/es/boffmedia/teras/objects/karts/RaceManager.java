package es.boffmedia.teras.objects.karts;

import com.google.gson.internal.LinkedTreeMap;
import com.mrcrayfish.vehicle.entity.PoweredVehicleEntity;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.net.Messages;
import es.boffmedia.teras.net.both.MessageCarTest;
import es.boffmedia.teras.net.clientOld.CMessageVerVideo;
import es.boffmedia.teras.objects_old.karts.Carrera;
import es.boffmedia.teras.objects_old.karts.Circuito;
import es.boffmedia.teras.objects_old.karts.Participante;
import es.boffmedia.teras.util.FileHelper;
import es.boffmedia.teras.util.MessageHelper;
import io.leangen.geantyref.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.sound.midi.Track;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class RaceManager {
    public Map<String, RaceTrack> tracks;
    public Map<String, Race> activeRaces;
    public Map<UUID, RaceParticipant> participants;


    public RaceManager(){
        tracks = new LinkedTreeMap<>();
        activeRaces = new LinkedTreeMap<>();
        participants = new LinkedTreeMap<>();

        loadTracks();
    }

    public void loadTracks(){
        Type token = new TypeToken<Map<String, RaceTrack>>() {}.getType();

        tracks = (LinkedTreeMap<String, RaceTrack>) FileHelper.readFile("config/teras/circuitos.json", token);

        for (Map.Entry<String, RaceTrack> entry : tracks.entrySet()) {
            Teras.LOGGER.info(entry.getKey() + " : " + entry.getValue());
        }
    }

    public void joinRace(String trackName, int laps, ServerPlayerEntity player) {
        if (!tracks.containsKey(trackName)) {
            return;
        }

        if (!activeRaces.containsKey(trackName)) {
            RaceTrack track = tracks.get(trackName);
            // Crear una nueva carrera
            Race carrera = new Race(track, laps);
            activeRaces.put(trackName, carrera);
        }

        Race race = activeRaces.get(trackName);
        if (participants.containsKey(player.getUUID())) {
            leaveRace(player);
        }

        MessageHelper.enviarMensaje(player, "Entrando en la carrera " + trackName + " con " + laps + " vueltas");

        RaceParticipant participant = new RaceParticipant(player, race);
        participants.put(player.getUUID(), participant);
        race.getParticipants().add(participant);

        for (RaceParticipant part : race.getParticipants()) {
            TextComponent mensaje = new StringTextComponent("Participantes: " + race.getParticipants().size());
            StringTextComponent strVotarInicio = new StringTextComponent(TextFormatting.GREEN + " [Votar inicio] ");
            Style estilo = strVotarInicio.getStyle()
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/karts votar"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Entrar en carrera")));

            StringTextComponent strCancelarVoto = new StringTextComponent(TextFormatting.RED + " [Cancelar voto] ");
            Style estiloCancelar = strCancelarVoto.getStyle()
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/karts cancelarvoto"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Cancelar voto")));


            StringTextComponent strSalir = new StringTextComponent(TextFormatting.RED + " [Salir] ");
            Style estiloSalir = strSalir.getStyle()
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/karts salir"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Salir de la carrera")));


            strVotarInicio.setStyle(estilo);
            strCancelarVoto.setStyle(estiloCancelar);
            strSalir.setStyle(estiloSalir);

            mensaje.append(strVotarInicio);
            mensaje.append(strCancelarVoto);
            mensaje.append(strSalir);

            part.getPlayer().sendMessage(mensaje, UUID.randomUUID());

        }
    }



    public void leaveRace(ServerPlayerEntity jugador) {
        MessageHelper.enviarMensaje(jugador, "Saliendo de la carrera");
        if (!participants.containsKey(jugador.getUUID())) {
            MessageHelper.enviarMensaje(jugador, "No estás en ninguna carrera");
            return;
        }

        Race race = participants.get(jugador.getUUID()).getRaceIn();
        race.getParticipants().removeIf(participante -> participante.getPlayer().getUUID().equals(jugador.getUUID()));
        participants.remove(jugador.getUUID());
        MessageHelper.enviarMensaje(jugador, "Has salido de la carrera");

        if (race.getParticipants().isEmpty()) {
            activeRaces.remove(race.getTrack().getName());
            MessageHelper.enviarMensaje(jugador, "La carrera ha sido eliminada al no tener participantes");

        }
    }

    public void startCountdown(String nombre) {
        if (!activeRaces.containsKey(nombre)) {
            return;
        }

        Race race = activeRaces.get(nombre);
        race.startCountdown();
    }

    public void startRace(String nombre) {
        if (!activeRaces.containsKey(nombre)) {
            return;
        }

        Race race = activeRaces.get(nombre);
        race.start();
    }


    public void voteStart(ServerPlayerEntity jugador) {
        System.out.println("Votando inicio");
        System.out.println(jugador.getUUID());
        System.out.println(participants);
        if (!participants.containsKey(jugador.getUUID())) {
            MessageHelper.enviarMensaje(jugador, "No estás en ninguna carrera");
            return;
        }
        Race race = participants.get(jugador.getUUID()).getRaceIn();

        Teras.getLogger().info(race.getTrack().getName());
        Teras.getLogger().info(race.getStartVotes());

        if(race.getStartVotes().contains(jugador.getUUID())){
            MessageHelper.enviarMensaje(jugador, "Ya has votado por este circuito");
            return;
        }
        race.getStartVotes().add(jugador.getUUID());

        if(race.getStartVotes().size() > race.getParticipants().size() / 2){
            race.startCountdown();
            raceBroadcast("Han votado suficientes participantes, iniciando carrera, la carrera comenzará en 3 segundos");
        }
    }


    public void unvoteStart(ServerPlayerEntity player) {
        if (!participants.containsKey(player.getUUID())) {
            MessageHelper.enviarMensaje(player, "No estás en ninguna carrera");
            return;
        }
        Race race = participants.get(player.getUUID()).getRaceIn();

        if(!race.getStartVotes().contains(player.getUUID())){
            MessageHelper.enviarMensaje(player, "No has votado por este circuito");
            return;
        }

        MessageHelper.enviarMensaje(player, "Has cancelado tu voto");
        race.getStartVotes().remove(player.getUUID());
    }

    public void raceBroadcast(String mensaje){
        for (Map.Entry<UUID, RaceParticipant> entry : participants.entrySet()) {
            RaceParticipant participant = entry.getValue();
            participant.getPlayer().sendMessage(new StringTextComponent(mensaje), UUID.randomUUID());
        }
    }

    public void listTracks(ServerPlayerEntity player) {
        for (Map.Entry<String, RaceTrack> entry : tracks.entrySet()) {
            player.sendMessage(new StringTextComponent(entry.getKey()), UUID.randomUUID());
        }
    }

    public void playerTick(ServerPlayerEntity jugador) {
        if (!participants.containsKey(jugador.getUUID())) {
            return;
        }
        RaceParticipant participant = participants.get(jugador.getUUID());
        participant.tick();
    }


    public void hitCar(ServerPlayerEntity player){
        Teras.LOGGER.info("GOLPEANDO COCHE");
        PoweredVehicleEntity vehicleEntity = (PoweredVehicleEntity) player.getVehicle();

        int tiempo = 2000;
        new Thread(() -> {
            assert vehicleEntity != null;
            vehicleEntity.setEngine(false);

            for(int i = 0; i < 8; i++){

                float f = MathHelper.wrapDegrees(vehicleEntity.yRot);
                float f2 = MathHelper.wrapDegrees(player.yRot);

                vehicleEntity.yRot = f+ 45f ;
                vehicleEntity.yRotO = f+ 45f ;
                player.yRot = f2+45f;

                vehicleEntity.push(5,10,5);

                Teras.LOGGER.info(vehicleEntity.yRot);
                try {
                    Thread.sleep(tiempo / 8);
                } catch (InterruptedException e) {
                    Teras.LOGGER.error(e);
                }
            }
            vehicleEntity.setEngine(true);
        }).start();
    }

}
