package es.allblue.lizardon.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.forge.ForgePlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.karts.Circuito;
import es.allblue.lizardon.objects.karts.Punto;
import es.allblue.lizardon.pixelmon.battle.LizardonBattleController;
import es.allblue.lizardon.pixelmon.battle.TeamManager;
import es.allblue.lizardon.pixelmon.frentebatalla.TorreBatallaController;
import es.allblue.lizardon.util.FileHelper;
import es.allblue.lizardon.util.MessageHelper;
import es.allblue.lizardon.util.PersistentDataFields;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.UUID;

public class FrenteBatallaCommand {
    private Circuito circuito;


    public FrenteBatallaCommand(CommandDispatcher<CommandSource> dispatcher){
        LiteralArgumentBuilder<CommandSource> literalBuilder = Commands.literal("frentebatalla")
                .then(registrarEquipo())
                .then(iniciar())
                .then(continuar())
                .then(pausar())
                ;

        dispatcher.register(literalBuilder);

    }

    private ArgumentBuilder<CommandSource,?> registrarEquipo() {
        return Commands.literal("registrarEquipo")
                .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("modalidad", StringArgumentType.string())
                .executes((command) -> {
                    ServerPlayerEntity player = EntityArgument.getPlayer(command, "player");
                    String modalidad = StringArgumentType.getString(command, "modalidad");
                    if(!TorreBatallaController.puedeIniciar(player)) return 0;

                    if(TeamManager.existsTeam(player, modalidad)){
                        int racha = player.getPersistentData().getInt(modalidad);
                        StringTextComponent component = new StringTextComponent(TextFormatting.RED + "Ya tienes un equipo registrado en esta modalidad, con una racha de " + racha);

                        IFormattableTextComponent reanudar = new StringTextComponent(TextFormatting.GREEN + " [Reanudar] ")
                                .setStyle(StringTextComponent.EMPTY.getStyle()
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/frenteBatalla iniciar \"" + modalidad + "\"" ))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Reanudar combate"))));

                        component.append(reanudar);
                        player.sendMessage(component, UUID.randomUUID());
                        return 0;
                    }

                    TorreBatallaController.registrarEquipo(modalidad, player);
                    return 1;
                })
            ));
    }

    private ArgumentBuilder<CommandSource,?> iniciar() {
        return Commands.literal("iniciar")
                .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("modalidad", StringArgumentType.string())
                .executes((command) -> {
                    Entity entity = command.getSource().getEntity();
                    if (!(entity instanceof ServerPlayerEntity)) return 0;
                    ServerPlayerEntity player = (ServerPlayerEntity) entity;
                    if(!TorreBatallaController.puedeIniciar(player)) return 0;
                    String modalidad = StringArgumentType.getString(command, "modalidad");
                    TorreBatallaController.reanudar(player, modalidad);

                    return 0;
                })
            ));
    }

    private ArgumentBuilder<CommandSource,?> pausar() {
        return Commands.literal("pausar")
                .executes((command) -> {
                    Entity entity = command.getSource().getEntity();
                    if (!(entity instanceof ServerPlayerEntity)) return 0;
                    ServerPlayerEntity player = (ServerPlayerEntity) entity;
                    if(TorreBatallaController.puedeIniciar(player)) return 0;

                    String modalidad = player.getPersistentData().getString(PersistentDataFields.EQUIPO_ACTIVO.label);
                    TorreBatallaController.pausar(player, modalidad);
                    return 0;
                });
    }

    private ArgumentBuilder<CommandSource,?> continuar() {
        return Commands.literal("continuar")
                .executes((command) -> {
                    Entity entity = command.getSource().getEntity();
                    if (!(entity instanceof ServerPlayerEntity)) return 0;
                    ServerPlayerEntity player = (ServerPlayerEntity) entity;
                    if(!TorreBatallaController.puedeIniciar(player)) return 0;

                    String modalidad = player.getPersistentData().getString(PersistentDataFields.EQUIPO_ACTIVO.label);
                    TorreBatallaController.iniciarCombatev2(player, modalidad);
                    return 0;
                });
    }


}
