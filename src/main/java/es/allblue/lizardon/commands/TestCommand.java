package es.allblue.lizardon.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class TestCommand {
    private static VoicechatServerApi SERVER_API;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("lizartest")
                .executes((command) -> {
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
