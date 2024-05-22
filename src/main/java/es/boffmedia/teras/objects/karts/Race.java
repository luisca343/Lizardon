package es.boffmedia.teras.objects.karts;

import com.mrcrayfish.vehicle.entity.EngineTier;
import com.mrcrayfish.vehicle.entity.PoweredVehicleEntity;
import com.mrcrayfish.vehicle.entity.vehicle.ATVEntity;
import com.mrcrayfish.vehicle.init.ModEntities;
import es.boffmedia.teras.net.Messages;
import es.boffmedia.teras.net.client.CMessageRacePositionChange;
import es.boffmedia.teras.util.MessageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class Race {
    private RaceTrack track;
    private int laps;
    private ArrayList<RaceParticipant> participants;
    private RaceStatus status;
    private ArrayList<UUID> startVotes;
    private long startTime;
    private long endTime;

    public Race(RaceTrack track, int laps){
        this.track = track;
        this.laps = laps;
        this.participants = new ArrayList<>();
        this.status = RaceStatus.WAITING_PLAYERS;
        this.startVotes = new ArrayList<>();
        this.startTime = 0;
        this.endTime = 0;
    }

    public void voteStart(UUID player){
        if (!startVotes.contains(player)){
            startVotes.add(player);
        }
        if (startVotes.size() == participants.size()){
            startCountdown();
        }
    }

    public void cancelVote(UUID player){
        startVotes.remove(player);
    }

    public void spawnVehicle(ServerPlayerEntity player, CoordinatePoint point, StartingDirection orientation) {
        World world = player.level;
        ATVEntity vehicleEntity = new ATVEntity(ModEntities.ATV.get(), world);
        vehicleEntity.setRequiresFuel(false);
        vehicleEntity.setColorRGB(1, 2, 3);
        vehicleEntity.setEngineTier(EngineTier.WOOD);
        vehicleEntity.setPos(point.getX() + 0.5f, point.getY() + 1, point.getZ() + 0.5f);
        vehicleEntity.disableFallDamage = true;

        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        vehicleEntity.setColorRGB(r,g,b);

        //vehicleEntity.setOwner(uuid);

        switch (orientation){
            case EAST:
                vehicleEntity.yRot = -90;
                break;
            case WEST:
                vehicleEntity.yRot = 90;
                break;
            case NORTH:
                vehicleEntity.yRot = 0;
                break;
            case SOUTH:
                vehicleEntity.yRot = 180;
                break;
        }

        world.addFreshEntity(vehicleEntity);
        vehicleEntity.getSeatTracker().setSeatIndex(0, player.getUUID());
        player.startRiding(vehicleEntity);
    }

    public void startCountdown(){
        this.status = RaceStatus.STARTING;
        int i = 0;
        for (RaceParticipant participant : participants) {
            // Teleport each participant to a position
            CoordinatePoint startingPoint = track.getStartingPoints().get(i);
            StartingDirection orientacion = track.getStartingDirection() == null ? StartingDirection.NORTH : track.getStartingDirection();
            spawnVehicle(participant.getPlayer(), startingPoint, orientacion);
            i++;
        }

        new Thread(() -> {
            try {
                status = RaceStatus.STARTING;
                for (int j = 3; j > 0; j--) {
                    for (RaceParticipant participant : participants) {
                        MessageHelper.enviarTitulo(participant.getPlayer(), j + "");
                    }
                    Thread.sleep(1000);
                }

                for (RaceParticipant participant : participants) {
                    MessageHelper.enviarTitulo(participant.getPlayer(), "GO!");
                    allowMove(participant.getPlayer().getUUID(), true);
                }
                start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void allowMove(UUID uuid, boolean move) {
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof PoweredVehicleEntity) {
            PoweredVehicleEntity powered = (PoweredVehicleEntity) vehicle;
            powered.setEngine(move);
        }
    }

    public void start() {
            this.status = RaceStatus.IN_PROGRESS;
            this.startTime = System.currentTimeMillis();

            participants.forEach(p -> p.setCurrentCheckpoint(0));

            calculatePositions();
    }

    public void calculatePositions(){
        new Thread(() -> {
            try {
                while (status == RaceStatus.IN_PROGRESS){
                    Collections.sort(participants,
                            Comparator.comparing(RaceParticipant::getCurrentLap)
                                    .thenComparing(RaceParticipant::getCurrentCheckpointIndex).reversed()
                    );
                    for (RaceParticipant participant : participants) {
                        if(participant.getFinishTime() == 0){
                            Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> participant.getPlayer()), new CMessageRacePositionChange(participants.indexOf(participant) + 1));
                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void checkPlayerInCheckpoint(ServerPlayerEntity player, CoordinatePoint point) {

        UUID uuid = player.getUUID();
        RaceParticipant participant = getParticipants().stream().filter(p -> p.getPlayer().getUUID().equals(uuid)).findFirst().orElse(null);
        if (participant == null) return;
        Checkpoint checkpoint = track.getCheckpoints().get(participant.getCurrentCheckpointIndex());
        if (checkpoint.isInCheckpoint(point)) {
            participant.nextCheckpoint();
            if (participant.getCurrentCheckpointIndex() == track.getCheckpoints().size()) {
                participant.setCurrentLap(participant.getCurrentLap() + 1);
                participant.setCurrentCheckpoint(0);
                MessageHelper.enviarMensaje(participant.getPlayer(), "Has completado una vuelta");
            }
            if(participant.getCurrentLap() > laps){
                participant.setFinishTime(System.currentTimeMillis());

                player.getVehicle().remove();
                MessageHelper.enviarMensaje(player, "Has terminado la carrera en " + MessageHelper.formatearTiempo(participant.getFinishTime() - startTime));
                Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> participant.getPlayer()), new CMessageRacePositionChange(0));
            }
        }
    }


    public ArrayList<Checkpoint> getCheckpoints() {
        return track.getCheckpoints();
    }

    public ArrayList<RaceParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<RaceParticipant> participants) {
        this.participants = participants;
    }

    public RaceTrack getTrack() {
        return track;
    }

    public void setTrack(RaceTrack track) {
        this.track = track;
    }

    public ArrayList<UUID> getStartVotes() {
        return startVotes;
    }

    public void setStartVotes(ArrayList<UUID> startVotes) {
        this.startVotes = startVotes;
    }

    public RaceStatus getStatus() {
        return status;
    }

    public void setStatus(RaceStatus status) {
        this.status = status;
    }
}
