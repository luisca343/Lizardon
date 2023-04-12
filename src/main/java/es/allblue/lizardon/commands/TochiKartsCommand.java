package es.allblue.lizardon.commands;


import com.mojang.brigadier.CommandDispatcher;
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
import es.allblue.lizardon.objects.tochikarts.CarreraManager;
import es.allblue.lizardon.objects.tochikarts.Circuito;
import es.allblue.lizardon.objects.tochikarts.Punto;
import es.allblue.lizardon.util.WingullAPI;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class TochiKartsCommand {
    private Circuito circuito;

    public TochiKartsCommand(CommandDispatcher<CommandSource> dispatcher){
        LiteralArgumentBuilder<CommandSource> literalBuilder = Commands.literal("tochikarts")
                .requires((commandSource -> commandSource.hasPermission(3))
                    ).then(Commands.literal("circuito")
                        .then(crearCircuito())
                        .then(nuevoCheckpoint())
                        .then(guardarCircuito())
                        .then(nuevoInicio())
                    ).then(Commands.literal("carrera")
                        .then(entrarCarrera())
                        .then(salirCarrera())
                        .then(iniciarCarrera())
                        .then(testC())


                );

        dispatcher.register(literalBuilder);

    }

    private ArgumentBuilder<CommandSource,?> testC() {
        return Commands.literal("test")
                .executes((command) -> {
                    command.getSource().getEntity().sendMessage(new StringTextComponent(WingullAPI.wingullGET("")), UUID.randomUUID());
                    return 1;
                });
    }

    private ArgumentBuilder<CommandSource,?> iniciarCarrera() {
        return Commands.literal("iniciar")
                .then(Commands.argument("nombre", StringArgumentType.string())
                        .executes((command) -> {
                            String nombre = StringArgumentType.getString(command, "nombre");
                            // Usar el CarreraManager para crear la carrera
                            CarreraManager.iniciarCarrera(nombre);

                            return 1;
                        })
                );
    }

    private ArgumentBuilder<CommandSource,?> salirCarrera() {
        return Commands.literal("salir")
                .then(Commands.argument("jugador", EntityArgument.player())
                        .executes((command) -> {
                            ServerPlayerEntity player = EntityArgument.getPlayer(command, "player");
                            // Usar el CarreraManager para crear la carrera
                            CarreraManager.salirCarrera(player);

                            return 1;
                        })
                );
    }

    private ArgumentBuilder<CommandSource,?> entrarCarrera() {
        return Commands.literal("entrar")
                .then(Commands.argument("nombre", StringArgumentType.string())
                        .then(Commands.argument("jugador", EntityArgument.player())
                        .executes((command) -> {
                            String nombre = StringArgumentType.getString(command, "nombre");
                            ServerPlayerEntity player = EntityArgument.getPlayer(command, "jugador");
                            // Usar el CarreraManager para crear la carrera
                            CarreraManager.entrarCarrera(nombre, player);


                            return 1;
                        })
                ));
    }


    private ArgumentBuilder<CommandSource,?> guardarCircuito() {
        return Commands.literal("guardar")
                .executes((command) -> {
                    CommandSource source = command.getSource();
                    if (circuito == null) {
                        source.sendFailure(new StringTextComponent("Crea un circuito primero con /tochikarts circuito crear <nombre>"));
                        return 0;
                    }

                    if(circuito.getCheckpoints().size() < 2){
                        source.sendFailure(new StringTextComponent("El circuito debe tener al menos 2 checkpoints"));
                        return 0;
                    }

                    if(circuito.getInicios().size() < 1){
                        source.sendFailure(new StringTextComponent("El circuito debe tener al menos 1 punto de inicio"));
                        return 0;
                    }

                    circuito.guardar();
                    source.sendSuccess(new StringTextComponent("Circuito guardado"), false);
                    return 1;
                });
    }

    public LiteralArgumentBuilder<CommandSource> crearCircuito(){
        return Commands.literal("crear")
                .then(Commands.argument("nombre", StringArgumentType.string())
                        .executes((command) -> {
                            String nombre = StringArgumentType.getString(command, "nombre");
                            circuito = new Circuito(nombre);
                            return 1;
                        })
                );
    }

    public LiteralArgumentBuilder<CommandSource> nuevoCheckpoint(){
        return Commands.literal("checkpoint")
                .executes((command) -> {
                    CommandSource source = command.getSource();
                    if (circuito == null) {
                        source.sendFailure(new StringTextComponent("Crea un circuito primero con /tochikarts circuito crear <nombre>"));
                        return 0;
                    }
                    ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                    SessionManager manager = WorldEdit.getInstance().getSessionManager();
                    ForgePlayer adaptedPlayer = ForgeAdapter.adaptPlayer(player);
                    try {
                        Region selection = manager.get(adaptedPlayer).getSelection();
                        BlockVector3 punto1 = selection.getMinimumPoint();
                        BlockVector3 punto2 = selection.getMaximumPoint();

                        Punto puntoInicio = new Punto(punto1.getX(), punto1.getY(), punto1.getZ());
                        Punto puntoFin = new Punto(punto2.getX(), punto2.getY(), punto2.getZ());
                        source.sendSuccess(new StringTextComponent("Añadiendo..."), false);
                        source.sendSuccess(new StringTextComponent(circuito.toString()), false);

                        circuito.nuevoCheckpoint(puntoInicio, puntoFin);
                        source.sendSuccess(new StringTextComponent("Checkpoint añadido"), false);

                        circuito.printCheckpoints(source);


                    } catch (IncompleteRegionException e) {
                        source.sendFailure(new StringTextComponent("Selecciona una región primero"));
                        e.printStackTrace();
                    }
                    return 1;
                });
    }


    private ArgumentBuilder<CommandSource,?> nuevoInicio() {
        return Commands.literal("inicio")
                .executes((command) -> {
                    CommandSource source = command.getSource();
                    if (circuito == null) {
                        source.sendFailure(new StringTextComponent("Crea un circuito primero con /tochikarts circuito crear <nombre>"));
                        return 0;
                    }
                    ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                    SessionManager manager = WorldEdit.getInstance().getSessionManager();
                    ForgePlayer adaptedPlayer = ForgeAdapter.adaptPlayer(player);
                    try {
                        Region selection = manager.get(adaptedPlayer).getSelection();
                        BlockVector3 v1 = selection.getMinimumPoint();

                        Punto punto = new Punto(v1.getX(), v1.getY(), v1.getZ());

                        source.sendSuccess(new StringTextComponent("Añadiendo..."), false);
                        source.sendSuccess(new StringTextComponent(circuito.toString()), false);

                        circuito.nuevoInicio(punto);
                        source.sendSuccess(new StringTextComponent("Inicio añadido"), false);

                        circuito.printCheckpoints(source);


                    } catch (IncompleteRegionException e) {
                        source.sendFailure(new StringTextComponent("Selecciona una región primero"));
                        e.printStackTrace();
                    }
                    return 1;
                });
    }

}
