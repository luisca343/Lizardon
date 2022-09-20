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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
