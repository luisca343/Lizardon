package es.allblue.lizardon.util;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
import es.allblue.lizardon.LizardonVoicechatPlugin;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AudioManagerOld {

    private static final byte[][] MP3_MAGIC_BYTES = new byte[][]{
            {(byte) 0xFF, (byte) 0xFB},
            {(byte) 0xFF, (byte) 0xF3},
            {(byte) 0xFF, (byte) 0xF2},
            {(byte) 0x49, (byte) 0x44, (byte) 0x33}
    };

    public static AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000F, 16, 1, 2, 48000F, false);

    @Nullable
    public static AudioType getAudioType(byte[] data) throws UnsupportedAudioFileException, IOException {
        if (hasMp3MagicBytes(data)) {
            return AudioType.MP3;
        }
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
        if (isWav(audioInputStream.getFormat())) {
            return AudioType.WAV;
        }
        return null;
    }

    @Nullable
    public static AudioType getAudioType(Path path) throws UnsupportedAudioFileException, IOException {
        if (AudioType.WAV.isValidFileName(path)) {
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(path.toFile())) {
                if (isWav(ais.getFormat())) {
                    return AudioType.WAV;
                }
            }
        } else if (AudioType.MP3.isValidFileName(path)) {
            if (isMp3File(path)) {
                return AudioType.MP3;
            }
        }
        return null;
    }


    private static boolean hasMp3MagicBytes(byte[] data) {
        for (byte[] magicBytes : MP3_MAGIC_BYTES) {
            if (data.length < magicBytes.length) {
                return false;
            }
            boolean valid = true;
            for (int i = 0; i < magicBytes.length; i++) {
                if (data[i] != magicBytes[i]) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMp3File(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            byte[] data = new byte[3];
            is.read(data, 0, 3);
            return hasMp3MagicBytes(data);
        }
    }


    public static boolean isWav(AudioFormat audioFormat) {
        AudioFormat.Encoding encoding = audioFormat.getEncoding();
        return encoding.equals(AudioFormat.Encoding.PCM_SIGNED) ||
                encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) ||
                encoding.equals(AudioFormat.Encoding.PCM_FLOAT) ||
                encoding.equals(AudioFormat.Encoding.ALAW) ||
                encoding.equals(AudioFormat.Encoding.ULAW);
    }


    private static short[] convert(AudioInputStream source) throws IOException {
        AudioFormat sourceFormat = source.getFormat();
        AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
        AudioInputStream stream1 = AudioSystem.getAudioInputStream(convertFormat, source);
        AudioInputStream stream2 = AudioSystem.getAudioInputStream(FORMAT, stream1);

        return LizardonVoicechatPlugin.SERVER_API.getAudioConverter().bytesToShorts(IOUtils.toByteArray(stream2));
    }




    public final Map<UUID, AudioPlayer> players;

    public AudioManagerOld(){
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
            AudioPlayer audioPlayer = api.createAudioPlayer(chan, api.createEncoder(), AudioManagerOld.readSound(getFile(file)));
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
            AudioPlayer player = api.createAudioPlayer(channel, api.createEncoder(), AudioManagerOld.readSound(file));
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

        return base.resolve(nombre);

    }

    public static void guardar(String url, String nombre, ServerWorld level) throws IOException, UnsupportedAudioFileException {
        byte[] data = download(new URL(url));
        AudioType type = null;


            AudioInputStream in = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            AudioSystem.write(in, AudioFileFormat.Type.WAVE, baos);

            type = getAudioType(data);
            checkExtensionAllowed(type);

            Path file = getFile(nombre, "wav");
            /*


            OutputStream outputStream = Files.newOutputStream(file);*/
            Files.createDirectories(file.getParent());

            data = baos.toByteArray();

            AudioSystem.write(AudioSystem.getAudioInputStream(new ByteArrayInputStream(data)), AudioFileFormat.Type.WAVE, Files.newOutputStream(file, StandardOpenOption.CREATE_NEW));



    }

    public static void checkExtensionAllowed(@Nullable AudioType audioType) throws UnsupportedAudioFileException {
        if (audioType == null) {
            throw new UnsupportedAudioFileException("Unsupported audio format");
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







    public enum AudioType {
        MP3("mp3"),
        WAV("wav");

        private final String extension;

        AudioType(String fileName) {
            this.extension = fileName;
        }

        public boolean isValidFileName(Path path) {
            return path.toString().toLowerCase().endsWith("." + extension);
        }

        public String getExtension() {
            return extension;
        }
    }

}
