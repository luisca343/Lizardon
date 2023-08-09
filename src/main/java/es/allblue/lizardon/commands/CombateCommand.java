package es.allblue.lizardon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import es.allblue.lizardon.objects.pixelmon.Combate;
import es.allblue.lizardon.objects.pixelmon.ConfigCombate;
import es.allblue.lizardon.util.Reader;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashMap;

public class CombateCommand {
    public static HashMap<Integer, Combate> combatesEspeciales = new HashMap<>();
    public static HashMap<Integer, ConfigCombate> combatesEntrenador = new HashMap<>();
    public static HashMap<Integer, ConfigCombate> combatesSalvajes = new HashMap<>();

    public CombateCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("combate")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("url", StringArgumentType.string())
                                .executes((command) -> {
                                    return iniciarCombate(command);

                                })
                        )));

        dispatcher.register(Commands.literal("encuentro")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("url", StringArgumentType.string())
                                .executes((command) -> {
                                    return iniciarEncuentro(command);
                                })
                        )));


    }

    private int iniciarEncuentro(CommandContext<CommandSource> command) {
        try{
            ServerPlayerEntity player = EntityArgument.getPlayer(command, "player");
            String url = StringArgumentType.getString(command, "url");
            ConfigCombate configSalvaje = Reader.getDatosEncuentro(url);
            Combate combate = new Combate(player, configSalvaje);
            combate.iniciarCombate();
        } catch (CommandSyntaxException e){
            System.out.println("Error al iniciar el combate");
        }
        return 1;
    }

    public int iniciarCombate(CommandContext<CommandSource> command) {
        try{
            ServerPlayerEntity player = EntityArgument.getPlayer(command, "player");
            String url = StringArgumentType.getString(command, "url");
            ConfigCombate configCombateEntrenador = Reader.getDatosNPC(url);
            System.out.println("Iniciando combate entrenador");
            System.out.println(configCombateEntrenador.toString());
            Combate combate = new Combate(player, configCombateEntrenador);
            combate.iniciarCombate();
        }catch(CommandSyntaxException e){
            System.out.println("Error al iniciar el combate");
        }

        return 1;
    }



}
