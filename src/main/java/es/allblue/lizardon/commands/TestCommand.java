package es.allblue.lizardon.commands;


import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.forge.ForgePlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageWaypoints;
import es.allblue.lizardon.objects.WayPoint;
import es.allblue.lizardon.objects.tochikarts.CarreraManager;
import es.allblue.lizardon.objects.tochikarts.Circuito;
import es.allblue.lizardon.objects.tochikarts.Punto;
import es.allblue.lizardon.util.music.AudioManager;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.awt.*;
import java.util.UUID;

public class TestCommand {
    private Circuito circuito;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> literalBuilder = Commands.literal("test")
                .requires((commandSource -> commandSource.hasPermission(3))
                ).then(crearWaypoint())
                .then(reproducirSonido())
                ;

        dispatcher.register(literalBuilder);

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