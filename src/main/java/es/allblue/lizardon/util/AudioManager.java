package es.allblue.lizardon.util;

import com.google.common.hash.BloomFilter;
import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
import es.allblue.lizardon.LizardonVoicechatPlugin;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.server.ServerWorld;
import net.montoyo.mcef.api.API;
import org.apache.commons.io.IOUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AudioManager {
    public static AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000F, 16, 1, 2, 48000F, false);

    public final Map<UUID, AudioPlayer> players;

    public AudioManager(){
        players = new ConcurrentHashMap<>();
    }

    public static short[] readSound(Path file) throws UnsupportedAudioFileException, IOException {

        Path test = Paths.get("musica");
        Path str = test.resolve("dolor.wav");
        

        AudioInputStream in = AudioSystem.getAudioInputStream(file.toFile());
        AudioInputStream converted = AudioSystem.getAudioInputStream(FORMAT, in);



        return LizardonVoicechatPlugin.SERVER_API.getAudioConverter().bytesToShorts(IOUtils.toByteArray(converted));
    }

    public void reproducirJugador(ServerPlayerEntity player, String file) {
        VoicechatServerApi api = LizardonVoicechatPlugin.SERVER_API;
        UUID channelID = UUID.randomUUID();
        VoicechatConnection con = api.getConnectionOf(player.getUUID());
        EntityAudioChannel channel = api.createEntityAudioChannel(channelID, api.fromServerPlayer(player));
        StaticAudioChannel chan = api.createStaticAudioChannel(UUID.randomUUID(), api.fromServerLevel(player.level), con);

        try {
            player.sendMessage(new StringTextComponent("Reproduciendo solo para ti..."), UUID.randomUUID());
            AudioPlayer audioPlayer = api.createAudioPlayer(chan, api.createEncoder(), AudioManager.readSound(getFile(file)));
            players.put(channelID, audioPlayer);
            audioPlayer.startPlaying();

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void play(ServerWorld level, Path file){
        VoicechatServerApi api = LizardonVoicechatPlugin.SERVER_API;
        UUID channelID = UUID.randomUUID();
        LocationalAudioChannel channel = api.createLocationalAudioChannel(channelID, api.fromServerLevel(level), api.createPosition(0 + 0.5D, 64 + 0.5D, 0 + 0.5D));

        api.getPlayersInRange(api.fromServerLevel(level), channel.getLocation(), api.getBroadcastRange(), serverPlayer -> {
            VoicechatConnection connection = api.getConnectionOf(serverPlayer);
            if (connection != null) {
                return connection.isDisabled();
            }
            return true;
        }).stream().map(Player::getPlayer).map(ServerPlayerEntity.class::cast).forEach(player -> {
            player.displayClientMessage(new StringTextComponent("You need to enable voice chat to hear this music disc"), true);
        });

        try {
            AudioPlayer player = api.createAudioPlayer(channel, api.createEncoder(), AudioManager.readSound(file));
            players.put(channelID, player);
            player.startPlaying();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path getFile(String nombre, String extension){
        Path base = Paths.get("musica");
        if(!base.toFile().exists()){
            base.toFile().mkdir();
        }

        return getFile(nombre+ "." + extension);

    }


    private static Path getFile(String nombre){
        Path base = Paths.get("musica");
        if(!base.toFile().exists()){
            base.toFile().mkdir();
        }

        System.out.println(nombre);
        System.out.println(base.resolve(nombre));
        return base.resolve(nombre);

    }

    public static void guardar(String url, String nombre, ServerWorld level) throws IOException {
        byte[] data = download(new URL(url));
        Path file = getFile(nombre, "wav");
        Files.createDirectories(file.getParent());
        try (OutputStream outputStream = Files.newOutputStream(file)) {
            IOUtils.write(data, outputStream);
        }finally {
            AudioManager manager = new AudioManager();
            manager.play(level, file);
        }
    }


    private static byte[] download(URL url) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        URLConnection openConnection = url.openConnection();

        openConnection.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        openConnection.connect();
        BufferedInputStream bis = new BufferedInputStream(openConnection.getInputStream());


        int nRead;
        byte[] data = new byte[32768];

        while ((nRead = bis.read(data, 0, data.length)) != -1) {
            bos.write(data, 0, nRead);
            /*
            if (bos.size() > limit) {
                bis.close();
                throw new IOException(String.format("Maximum file size of %sMB exceeded", (int) (((float) limit) / 1_000_000F)));
            }*/
        }
        bis.close();
        return bos.toByteArray();
    }

}
