package es.allblue.lizardon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.server.SMessageVerMisiones;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class TestCommand {
    private static VoicechatServerApi SERVER_API;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("lizartest")
                .executes((command) -> {
                    return testCommand(command.getSource());
        }));
    }

    public int testCommand(CommandSource source) throws CommandSyntaxException {
        System.out.println("EL COMANDO");
        ServerPlayerEntity player = source.getPlayerOrException();
        System.out.println(player.getStringUUID());
        NpcAPI api = NpcAPI.Instance();
        PlayerWrapper wrapper = new PlayerWrapper(player);
        IQuestObjective obyectivo = api.getQuests().get(3).getObjectives(wrapper)[0];
        obyectivo.setProgress(obyectivo.getProgress()+1);
        IQuestHandler questHandler = api.getQuests();

        for ( IQuestCategory category : questHandler.categories()) {
            Lizardon.getLogger().info(category.getName());
            for ( IQuest quest : category.quests()) {
                Lizardon.getLogger().info(quest.getName());
            }
        }

        return 1;
    }
}
