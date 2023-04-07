package es.allblue.lizardon.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import es.allblue.lizardon.objects.tochikarts.Circuito;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class CarreraCommand {
    private Circuito circuito;

    public CarreraCommand(CommandDispatcher<CommandSource> dispatcher){
        LiteralArgumentBuilder<CommandSource> literalBuilder = Commands.literal("carrera")
                .requires((commandSource -> commandSource.hasPermission(3)));

        literalBuilder.then(Commands.literal("crear")
           .executes((command) -> {
               circuito = new Circuito();
               ServerPlayerEntity player = EntityArgument.getPlayer(command, "player");

               player.displayClientMessage(new StringTextComponent("Creando circuito"), true);

               player.sendMessage(new StringTextComponent("Creando circuito"), null);
               return 1;
           })
        );

        literalBuilder.then(Commands.literal("checkpoint")
                .executes((command) -> {
                    ServerPlayerEntity player = EntityArgument.getPlayer(command, "player");

                    return 1;
                })
        );


        dispatcher.register(literalBuilder);
    }
}
