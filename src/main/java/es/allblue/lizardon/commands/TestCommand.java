package es.allblue.lizardon.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.ExampleVoicechatPlugin;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class TestCommand {
    private static VoicechatServerApi SERVER_API;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("lizartest")
                .executes((command) -> {
            System.out.println("TEST");

            SERVER_API = ExampleVoicechatPlugin.SERVER_API;
                    Group group = SERVER_API.createGroup("Lizardon", null);


            ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName("Luisca343");
            VoicechatConnection conn = SERVER_API.getConnectionOf(player.getUUID());


            conn.setGroup(group);
            ServerPlayerEntity player2 = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName("SrKamina");
            VoicechatConnection conn2 = SERVER_API.getConnectionOf(player2.getUUID());
            conn2.setGroup(group);

            System.out.println("Añadido a grupo");
            return 0;
        }));
    }
}
