package es.allblue.lizardon.commands;


import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mrcrayfish.vehicle.network.PacketHandler;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageVerVideo;
import es.allblue.lizardon.net.client.CMessageWaypoints;
import es.allblue.lizardon.objects.WayPoint;
import es.allblue.lizardon.objects.karts.Circuito;
import es.allblue.lizardon.util.music.AudioManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public class TestCommand {
    private Circuito circuito;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> literalBuilder = Commands.literal("test")
                .then(playVideo())
                .requires((commandSource -> commandSource.hasPermission(3))
                ).then(crearWaypoint())
                .then(reproducirSonido())
                .then(testset())
                ;

        dispatcher.register(literalBuilder);

    }

    private ArgumentBuilder<CommandSource,?> playVideo() {
        return Commands.literal("playvideo")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("url", StringArgumentType.string())
                        .executes((command) -> {
                            ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                            String url = StringArgumentType.getString(command, "url");
                            Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageVerVideo(url));
                            return 1;
                        }
                )));
    }

    private ArgumentBuilder<CommandSource,?> testset() {
        return Commands.literal("testset")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes((command) -> {

                            return 1;
                        }
                ));
    }

    private ArgumentBuilder<CommandSource,?> reproducirSonido() {
        return Commands.literal("reproducir")
                        .executes((command) -> {
                            ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                            AudioManager audioManager = new AudioManager();
                            audioManager.playMp3(player);
                            return 1;
                        }
                );
    }

    private ArgumentBuilder<CommandSource, ?> crearWaypoint() {
        return Commands.literal("waypoint")
                .then(Commands.argument("x", IntegerArgumentType.integer())
                .then(Commands.argument("y", IntegerArgumentType.integer())
                .then(Commands.argument("z", IntegerArgumentType.integer())
                .then(Commands.argument("nombre", StringArgumentType.string())
                .then(Commands.argument("color", StringArgumentType.string())
                .executes((command) -> {
                    command.getSource().getEntity().sendMessage(new StringTextComponent("ESTO ES UNA PRUEBA"), UUID.randomUUID());
                    ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                    int x = IntegerArgumentType.getInteger(command, "x");
                    int y = IntegerArgumentType.getInteger(command, "y");
                    int z = IntegerArgumentType.getInteger(command, "z");
                    String nombre = StringArgumentType.getString(command, "nombre");
                    String color = StringArgumentType.getString(command, "color");

                    WayPoint waypoint = new WayPoint( x, y, z, "world",color, nombre);
                    Gson gson = new Gson();
                    Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageWaypoints(gson.toJson(waypoint)));

                    return 1;
                }))))));
    }
}