package es.allblue.lizardon.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.util.AudioManagerOld;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.storage.FolderName;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Discos {
    private static VoicechatServerApi SERVER_API;
    public  static FolderName DISCOS = new FolderName("discos");
    public static AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000F, 16, 1, 2, 48000F, false);

    public Discos(CommandDispatcher<CommandSource> dispatcher){
        LiteralArgumentBuilder<CommandSource> literalBuilder = Commands.literal("sonido")
                .requires((commandSource -> commandSource.hasPermission(3)));

        literalBuilder.then(Commands.literal("descargar")
                        .then(Commands.argument("url", StringArgumentType.string())
                        .then(Commands.argument("nombre", StringArgumentType.string())
                                .executes((command) -> {

                                    CommandSource source = command.getSource();
                            String url = command.getArgument("url", String.class);
                            String nombre = command.getArgument("nombre", String.class);

                                    new Thread(() -> {
                                        try {
                                            source.sendSuccess(new StringTextComponent("Descargando, por favor espera..."), false);
                                            AudioManagerOld.guardar(url, nombre, command.getSource().getLevel());
                                            source.sendSuccess(new StringTextComponent("¡Archivo descargado!"), false);

                                        } catch (IOException e) {
                                            source.sendFailure(new StringTextComponent("Error descargando el archivo: " + e.getMessage()));
                                            e.printStackTrace();
                                        } catch (UnsupportedAudioFileException e) {
                                            source.sendFailure(new StringTextComponent("Error. Formato no soportado: " + e.getMessage()));
                                            e.printStackTrace();
                                        }
                                    }).start();


                            //manager.play(command.getSource().getLevel());

                            return 1;
                        })
                    )));


        literalBuilder.then(Commands.literal("jugador")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("nombre", StringArgumentType.string())
                                .executes((command) -> {
                                    ServerPlayerEntity player = EntityArgument.getPlayer(command, "player");

                                    String nombre = command.getArgument("nombre", String.class);
                                    AudioManagerOld manager = new AudioManagerOld();
                                    manager.reproducirJugador(player, nombre);
                                    //manager.play(command.getSource().getLevel());

                                    return 1;
                                })
                        )));


        dispatcher.register(literalBuilder);
    }
}
