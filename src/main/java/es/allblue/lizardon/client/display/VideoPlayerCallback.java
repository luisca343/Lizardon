package es.allblue.lizardon.client.display;

import javafx.scene.media.MediaPlayer;
import me.lib720.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;


public interface VideoPlayerCallback extends Executor {
    void onVideoFrame(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat);

    @Override
    default void execute(Runnable command) {
        new Thread(command).start();
    }
}