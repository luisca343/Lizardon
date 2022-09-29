package es.allblue.lizardon.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.server.SMessageIniciarLlamada;
import es.allblue.lizardon.net.server.SMessageVerMisiones;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.wrapper.PlayerWrapper;

import java.util.List;

public class TestCommand {
    private static VoicechatServerApi SERVER_API;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("lizartest")
                .executes((command) -> {
                    NpcAPI api = NpcAPI.Instance();

                    Messages.INSTANCE.sendToServer(new SMessageVerMisiones("test"));

                    /*
                    try {
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Lizardon/musica/denden.mp3"));
                        Media hit = new Media(new File(bip).toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(hit);
                        mediaPlayer.play();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }*/
                    return 0;
        }));
    }
}
