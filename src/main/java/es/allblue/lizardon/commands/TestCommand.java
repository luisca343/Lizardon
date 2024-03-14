package es.allblue.lizardon.commands;


import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.pokemon.species.dimensions.Dimensions;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.SpeciesHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.StatueEntity;
import com.pixelmonmod.pixelmon.enums.EnumBoundingBoxMode;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.command.WorldEditCommands;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.forge.ForgePlayer;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.RegionMaskingFilter;
import com.sk89q.worldedit.function.biome.BiomeReplace;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.Component;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.TranslatableComponent;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.biome.BiomeType;
import es.allblue.lizardon.init.ModBiomes;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageVerVideo;
import es.allblue.lizardon.net.client.CMessageWaypoints;
import es.allblue.lizardon.net.video.ScreenManager;
import es.allblue.lizardon.objects.WayPoint;
import es.allblue.lizardon.objects.karts.Circuito;
import es.allblue.lizardon.objects.karts.Punto;
import es.allblue.lizardon.pixelmon.battle.LizardonBattleController;
import es.allblue.lizardon.pixelmon.battle.TeamManager;
import es.allblue.lizardon.pixelmon.frentebatalla.TorreBatallaController;
import es.allblue.lizardon.util.FileHelper;
import es.allblue.lizardon.util.MessageHelper;
import es.allblue.lizardon.util.PersistentDataFields;
import es.allblue.lizardon.util.music.AudioManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.*;
import java.util.*;

public class TestCommand {
    private Circuito circuito;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> literalBuilder = Commands.literal("test")
                .then(broadcast())
                .then(peluche())
                .then(registrarEquipo())
                .then(testFB())
                .then(baseSecreta())
                .then(guardarBase())
                .then(guardarEquipo())
                .then(cargarEquipo())
                .then(iniciarCombateFrenteBatalla())
                .then(playVideo())
                .requires((commandSource -> commandSource.hasPermission(3))
                ).then(crearWaypoint())
                .then(reproducirSonido())
                .then(testset());

        dispatcher.register(literalBuilder);

    }



    private ArgumentBuilder<CommandSource,?> broadcast() {
        return Commands.literal("broadcast")
                .then(Commands.argument("canal", IntegerArgumentType.integer())
                 .then(Commands.argument("url", StringArgumentType.string())
                .executes((command) -> {
                    String url = StringArgumentType.getString(command, "url");
                    int canal = IntegerArgumentType.getInteger(command, "canal");

                    ScreenManager.broadcastVideo(url, canal, command.getSource().getLevel());
                    return 1;
                })));
    }

    private ArgumentBuilder<CommandSource,?> peluche() {
        return Commands.literal("peluche")
                .then(Commands.argument("nombre", StringArgumentType.string())
                .executes((command) -> {
                    ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                    String nombre = StringArgumentType.getString(command, "nombre").toLowerCase(Locale.ROOT);
                    StatueEntity entity = new StatueEntity(player.level);
                    entity.setPos(player.getX(), player.getY(), player.getZ());
                    
                    if(!PixelmonSpecies.get(nombre).isPresent()) {
                        MessageHelper.enviarMensaje(player, "No existe el pokemon " + nombre);
                        return 1;
                    };

                    Species species = PixelmonSpecies.get(nombre).get().getValue().get();
                    Dimensions dim = species.getDefaultForm().getDimensions();
                    entity.setSpecies(species);
                    entity.setForm(species.getDefaultForm());
                    entity.setBoundingMode(EnumBoundingBoxMode.None);

                    double mayor = dim.getHeight();
                    if(dim.getLength() > mayor){
                        mayor = dim.getLength();
                    }

                    float escala = (float) (1 / mayor);
                    entity.setPixelmonScale(escala);


                    player.level.addFreshEntity(entity);
                    return 1;
                }));
    }
    
    private ArgumentBuilder<CommandSource,?> testFB() {
        return Commands.literal("testFB")
                .executes((command) -> {
                    ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                    if(player.getPersistentData().getBoolean(PersistentDataFields.FB_ACTIVO.label)) {
                        String modalidad = player.getPersistentData().getString(PersistentDataFields.EQUIPO_ACTIVO.label);
                        TorreBatallaController.iniciarCombatev2(player, modalidad);
                        MessageHelper.enviarMensaje(player, "Actualmetne participando en: " + player.getPersistentData().getString(PersistentDataFields.FB_ACTIVO.label));
                    } else{
                        MessageHelper.enviarMensaje(player, "No tienes un equipo registrado en el frente de batalla");
                    }
                    return 1;
                });
    }

    private ArgumentBuilder<CommandSource,?> registrarEquipo() {
        return Commands.literal("registrarEquipo")
                        .executes((command) -> {
                            ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                            TorreBatallaController.registrarEquipo(LizardonBattleController.TipoCombate.TB_INDIVIDUAL.label, player);
                            return 1;
                        });
    }

    public int parcelasCreadas = 0;
    public BlockVector3 inicio = BlockVector3.at(50, 100, 100);

    private ArgumentBuilder<CommandSource,?> baseSecreta() {
        return Commands.literal("baseSecreta")
                .executes((command) -> {
                    PlayerEntity player = (PlayerEntity) command.getSource().getEntity();
                    File schem = FileHelper.getSchematic("baseSecreta");
                    try {
                        ClipboardReader reader = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getReader(new FileInputStream(schem));
                        Clipboard clipboard = reader.read();


                        World world = ForgeAdapter.adapt(player.level);
                        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);

                        BlockVector3 inicioNueva = inicio.add(0, 0, 15 * parcelasCreadas);
                        Operation operation = new ClipboardHolder(clipboard)
                                .createPaste(editSession)
                                .to(inicioNueva)
                                .ignoreAirBlocks(false)
                                .build();
                        try {
                            Operations.complete(operation);
                            MessageHelper.enviarMensaje(player, "Parcela creada: " + ++parcelasCreadas);
                            editSession.flushSession();
                        } catch (WorldEditException e) {
                            e.printStackTrace();
                        }

                        return 1;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
    }

    private ArgumentBuilder<CommandSource,?> guardarBase() {
        return Commands.literal("guardarBase")
                .executes((command) -> {
                        PlayerEntity player = (PlayerEntity) command.getSource().getEntity();

                    World world = ForgeAdapter.adapt(player.level);
                    BlockVector3 pos1 = BlockVector3.at(0, 0, 0);
                    BlockVector3 pos2 = BlockVector3.at(10, 10, 10);

                    CuboidRegion region = new CuboidRegion(world, pos1, pos2);
                    Clipboard clipboard = new BlockArrayClipboard(region);


                    ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                            world, region, clipboard, region.getMinimumPoint()
                    );


                    try {
                        Operations.complete(forwardExtentCopy);
                        MessageHelper.enviarMensaje(player, "OPERACION COMPLETADA, BLOQUES AFECTADOS: " + forwardExtentCopy.getAffected());

                        File schem = FileHelper.getSchematic("baseSecreta");


                        ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schem));
                        writer.write(clipboard);
                        writer.close();


                    } catch (WorldEditException e) {
                        throw new RuntimeException(e);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return 1;

                });
    }

    private ArgumentBuilder<CommandSource,?> iniciarCombateFrenteBatalla() {
        return Commands.literal("iniciarCombateFrenteBatalla")
                .executes((command) -> {
                    ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                    TorreBatallaController.iniciarCombate(player);


                    return 1;
                });
    }

    private ArgumentBuilder<CommandSource,?> cargarEquipo() {
        return Commands.literal("cargarEquipo")
                .then(Commands.argument("nombre", StringArgumentType.string())
                .executes((command) -> {
                    ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                    String nombre = StringArgumentType.getString(command, "nombre");

                    TeamManager.loadTeam(player, "equipo");
                    player.getPersistentData().putBoolean(PersistentDataFields.FB_ACTIVO.label, false);

                    return 1;
                }));
    }

    private ArgumentBuilder<CommandSource,?> guardarEquipo() {
        return Commands.literal("guardarEquipo")
                .then(Commands.argument("nombre", StringArgumentType.string())
                .executes((command) -> {
                       ServerPlayerEntity player = (ServerPlayerEntity) command.getSource().getEntity();
                       String nombre = StringArgumentType.getString(command, "nombre");

                       TeamManager.saveTeam(player, nombre);

              return 1;
           }
           ));
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