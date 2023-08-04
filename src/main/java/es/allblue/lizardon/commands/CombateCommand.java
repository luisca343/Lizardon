package es.allblue.lizardon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTiers;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.api.PokePasteReader;
import es.allblue.lizardon.objects.Entrenador;
import es.allblue.lizardon.util.MessageUtil;
import es.allblue.lizardon.util.Reader;
import es.allblue.lizardon.util.Scoreboard;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.wrapper.NPCWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CombateCommand {
    public static HashMap<UUID, ICustomNpc> combatesActivos = new HashMap<UUID, ICustomNpc>();
    public static HashMap<UUID, String> encuentrosActivos = new HashMap<UUID, String>();

    public CombateCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("combate")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("url", StringArgumentType.string())
                                .executes((command) -> {
                                    return testCommand(command);

                                })
                        )));

        dispatcher.register(Commands.literal("encuentro")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("url", StringArgumentType.string())
                                .executes((command) -> {
                                    return testEncounter(command);
                                })
                        )));


    }

    private int testEncounter(CommandContext<CommandSource> command) {
        try{
            CommandSource source = (CommandSource) command.getSource();


            ServerPlayerEntity player = EntityArgument.getPlayer(command, "player");
            String url = StringArgumentType.getString(command, "url");

            url = "eventos/" + url;

            List<Pokemon> team = PokePasteReader.fromLizardon(url).build();
            Pokemon pkm = team.get(0);
            System.out.println(pkm.getDisplayName());

            PixelmonEntity pixelmon = pkm.getOrCreatePixelmon();
            player.level.addFreshEntity(pixelmon);

            WildPixelmonParticipant wildPart = new WildPixelmonParticipant(pixelmon);


            PlayerPartyStorage party = StorageProxy.getParty(player);
            List<Pokemon> pokemon = party.findAll(Pokemon::canBattle);
            PlayerParticipant partJugador = new PlayerParticipant(player, pokemon, 1);

            BattleRules br = new BattleRules();
            BattleController battle = BattleRegistry.startBattle(new BattleParticipant[]{partJugador}, new BattleParticipant[]{wildPart}, br);

            encuentrosActivos.put(player.getUUID(), url);
            MessageUtil.enviarMensaje(player, TextFormatting.LIGHT_PURPLE + "¡Un " + pkm.getDisplayName() + TextFormatting.LIGHT_PURPLE + " salvaje ha aparecido!");

        }catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    public int testCommand(CommandContext ctx) {
        try{
            CommandSource source = (CommandSource) ctx.getSource();

            String url = (String) ctx.getArgument("url", String.class);
            ServerPlayerEntity player = EntityArgument.getPlayer(ctx, "player");

            // Get the player team
            PlayerPartyStorage party = StorageProxy.getParty(player);
            List<Pokemon> pokemon = party.findAll(Pokemon::canBattle);
            party.getHighestLevel();

            /* Preparar equipo rival */

            NPCTrainer npc = new NPCTrainer(player.level);
            npc.setName("Entrenador");
            npc.greeting = url;

            Entrenador entrenador = Reader.getDatosNPC(url);


            try{
                ICustomNpc inpc = (ICustomNpc) NpcAPI.Instance().getIEntity(source.getEntity());
                combatesActivos.put(player.getUUID(), inpc);
            }catch (ClassCastException e){
                e.printStackTrace();
            }

            // Curar el equipo del jugador si es necesario
            if(entrenador.curar()){
                party.heal();
            }

            // Setear el nivel del entrenador
            String nivel = entrenador.getNivel();
            int maxLVL = 0;
            int lvl = 0;
            try {
                maxLVL = party.getHighestLevel();
                lvl = Integer.parseInt(entrenador.getNivel());
                nivel = BossTiers.NOT_BOSS;
            } catch (NumberFormatException nfe) {}

            npc.setBossTier(BossTierRegistry.getBossTierOrNotBoss(nivel));
            npc.setBattleAIMode(entrenador.getIA());

            List<Pokemon> team = PokePasteReader.fromLizardon(url).build();
            int i = 0;
            for (Pokemon pkm : team) {
                pkm.getPokemonLevelContainer().setLevel(maxLVL + lvl);
                npc.getPokemonStorage().set(i, pkm);

                i++;
                if (i == 6) break;
            }


            TrainerParticipant part2 = new TrainerParticipant(npc, 1);

            player.sendMessage(new StringTextComponent("INICIANDO BATALLA"), player.getUUID());
            BattleRules br = new BattleRules();

            /*
            PropertyValue<?> teamPreview = BattleRuleRegistry.getProperty("TeamPreview").getInstance();
            teamPreview.set(new BooleanValue(true));

            br.set(BattleRuleRegistry.TEAM_PREVIEW, teamPreview);
            */


            br.setNewClauses(entrenador.getNormas());
            PlayerParticipant partJugador = new PlayerParticipant(player, pokemon, 1);


            Scoreboard.init(player, url);
            BattleController battle = BattleRegistry.startBattle(new BattleParticipant[]{partJugador}, new BattleParticipant[]{part2}, br);

        }catch(Exception e){
            e.printStackTrace();
        }

        return 1;
    }



}
