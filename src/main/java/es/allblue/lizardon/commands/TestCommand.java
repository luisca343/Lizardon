package es.allblue.lizardon.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrcrayfish.vehicle.VehicleMod;
import com.mrcrayfish.vehicle.entity.EngineTier;
import com.mrcrayfish.vehicle.entity.VehicleEntity;
import com.mrcrayfish.vehicle.entity.VehicleProperties;
import com.mrcrayfish.vehicle.entity.vehicle.GoKartEntity;
import com.mrcrayfish.vehicle.init.ModEntities;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTier;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTiers;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.BattleBuilder;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleProperty;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.api.rules.PropertyValue;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.property.BattleTypeProperty;
import com.pixelmonmod.pixelmon.battles.api.rules.property.TeamPreviewProperty;
import com.pixelmonmod.pixelmon.battles.api.rules.value.BooleanValue;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.command.impl.Battle2;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.TrainerData;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.api.PokePasteReader;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageDatosServer;
import es.allblue.lizardon.net.client.CMessageVerVideo;
import es.allblue.lizardon.objects.Entrenador;
import es.allblue.lizardon.objects.pixelmon.PokemonData;
import es.allblue.lizardon.util.Reader;
import es.allblue.lizardon.util.Scoreboard;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestCommand {
    private static VoicechatServerApi SERVER_API;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("lizartest")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("url", StringArgumentType.string())
                                .executes((command) -> {
                                    return testCommand(command);

                                })
                        )));


        LiteralArgumentBuilder<CommandSource> testBuilder = Commands.literal("test")
                .requires((commandSource -> commandSource.hasPermission(3)))
                .executes((command) -> {
                    command.getSource().getEntity().sendMessage(new StringTextComponent("Comando de Test 2"), UUID.randomUUID());
                    return 1;
                });
        dispatcher.register(testBuilder);

        LiteralArgumentBuilder<CommandSource> test2Builder = Commands.literal("test2")
                .requires((commandSource -> commandSource.hasPermission(3)))
                .executes((command) -> {
                    command.getSource().getEntity().sendMessage(new StringTextComponent("Comando de Test 2"), UUID.randomUUID());
                    return 1;
                });
        dispatcher.register(test2Builder);

    }

    public int testCommand(CommandContext ctx) {
        try{
            CommandSource source = (CommandSource) ctx.getSource();

            String url = (String) ctx.getArgument("url", String.class);
            ServerPlayerEntity player = EntityArgument.getPlayer(ctx, "player");



        }catch(Exception e){
            e.printStackTrace();
        }

        return 1;
    }



}
