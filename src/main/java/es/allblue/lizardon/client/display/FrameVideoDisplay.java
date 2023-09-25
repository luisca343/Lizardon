package es.allblue.lizardon.client.display;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.texture.TextureCache;
import javafx.scene.media.MediaPlayer;
import me.lib720.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import me.srrapero720.watermedia.api.WaterMediaAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import team.creative.creativecore.common.util.math.vec.Vec3d;



import me.srrapero720.watermedia.api.player.SyncVideoPlayer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class FrameVideoDisplay extends FrameDisplay {
    private static final int ACCEPTABLE_SYNC_TIME = 1000;
    
    private static final List<FrameVideoDisplay> OPEN_DISPLAYS = new ArrayList<>();
    
    public static void tick() {
        synchronized (OPEN_DISPLAYS) {
            for (FrameVideoDisplay display : OPEN_DISPLAYS) {
                if (Minecraft.getInstance().isPaused())
                    if (display.stream) {

                        if (display.player.isPlaying())
                            display.player.setPauseMode(true);
                    } else if (display.player.getDuration() > 0 && display.player.isPlaying())
                        display.player.setPauseMode(true);
            }
        }
    }
    
    public static void unload() {
        synchronized (OPEN_DISPLAYS) {
            for (FrameVideoDisplay display : OPEN_DISPLAYS)
                display.free();
            OPEN_DISPLAYS.clear();
        }
    }
    
    public static FrameDisplay createVideoDisplay(Vec3d pos, String url, float volume, float minDistance, float maxDistance, boolean loop) {
        if (WaterMediaAPI.vlc_isReady()) {
            FrameVideoDisplay display = new FrameVideoDisplay(pos, url, volume, minDistance, maxDistance, loop);
            OPEN_DISPLAYS.add(display);
            return display;
        } else {
            TextureCache cache = new TextureCache(true ? WaterMediaAPI.img_getLoading().image : WaterMediaAPI.img_getFailedVLC().image);
            if (cache.ready()) return cache.createDisplay(pos, null, volume, minDistance, maxDistance, loop, true);
        }
        return null;
    }
    
    public volatile int width = 1;
    public volatile int height = 1;
    
    public SyncVideoPlayer player;
    
    private final Vec3d pos;
    private volatile IntBuffer buffer;
    public int texture;
    private boolean stream = false;
    private volatile float lastSetVolume;
    private volatile boolean needsUpdate = false;
    private final ReentrantLock lock = new ReentrantLock();
    private volatile boolean first = true;
    private long lastCorrectedTime = Long.MIN_VALUE;


    public FrameVideoDisplay(Vec3d pos, String url, float volume, float minDistance, float maxDistance, boolean loop) {
        super();
        this.pos = pos;
        texture = GlStateManager._genTexture();

        SyncVideoPlayer.BufferHelper bufferHelper = new SyncVideoPlayer.BufferHelper() {
            @Override
            public ByteBuffer create(int i) {
                return null;
            }

            public void allocateBuffers(ByteBuffer[] buffers, int width, int height) {
                lock.lock();
                try {
                    FrameVideoDisplay.this.width = width;
                    FrameVideoDisplay.this.height = height;
                    FrameVideoDisplay.this.first = true;
                    needsUpdate = true;
                } finally {
                    lock.unlock();
                }
                buffer.put(buffers[0].asIntBuffer());
                buffer.rewind();
                needsUpdate = true;
            }
        };

        player = new SyncVideoPlayer(null, new VideoPlayerCallback() {
            @Override
            public void onVideoFrame(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
                lock.lock();
                try {
                    buffer.put(nativeBuffers[0].asIntBuffer());
                    buffer.rewind();
                    needsUpdate = true;
                } finally {
                    lock.unlock();
                }
            }
        }, bufferHelper);

        float tempVolume = getVolume(volume, minDistance, maxDistance);
        player.setVolume((int) tempVolume);
        lastSetVolume = tempVolume;
        player.setRepeatMode(loop);

        // Execute the player start method on a new thread using this executor
        execute(() -> player.start(url));
    }

    public void execute(Runnable command) {
        new Thread(command).start();
    }

    
    public int getVolume(float volume, float minDistance, float maxDistance) {
        if (player == null)
            return 0;
        float distance = (float) getDistanceBetweenPositions(pos, Minecraft.getInstance().player.getPosition(Lizardon.PROXY.getFrameTime()));
        if (minDistance > maxDistance) {
            float temp = maxDistance;
            maxDistance = minDistance;
            minDistance = temp;
        }
        
        if (distance > minDistance)
            if (distance > maxDistance)
                volume = 0;
            else
                volume *= 1 - ((distance - minDistance) / (maxDistance - minDistance));
        return (int) (volume * 100F);
    }

    public static double getDistanceBetweenPositions(Vec3d pos1, Vector3d pos2) {
        double dx = pos1.x - pos2.x;
        double dy = pos1.y - pos2.y;
        double dz = pos1.z - pos2.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public void tick(String url, float volume, float minDistance, float maxDistance, boolean playing, boolean loop, int tick) {
        if (player == null)
            return;
        
        volume = getVolume(volume, minDistance, maxDistance);
        if (volume != lastSetVolume) {
            player.setVolume((int) volume);
            lastSetVolume = volume;
        }

        // IMPORTANT: WaterMedia changes behavior of this method trying to avoid deadlocks on ArchLinux
        // More info: https://github.com/SrRapero720/watermedia/issues/3
        if (player.isValid()) {
            boolean realPlaying = playing && !Minecraft.getInstance().isPaused();
            
            if (player.getRepeatMode() != loop) player.setRepeatMode(loop);
            long tickTime = 50;
            // This doesn't works... you can try play a Twitch video and looks pretty laggy
            // long newDuration = player.getDuration();
            // if (!stream && newDuration != -1 && newDuration != 0 && player.getMediaInfoDuration() == 0) stream = true;
            if (player.isLive()) {
                if (player.isPlaying() != realPlaying)
                    player.setPauseMode(!realPlaying);
            } else {
                if (player.getDuration() > 0) {
                    if (player.isPlaying() != realPlaying)
                        player.setPauseMode(!realPlaying);
                    
                    if (player.isValid()) {
                        long time = tick * tickTime + (realPlaying ? (long) (Lizardon.PROXY.getFrameTime() * tickTime) : 0);
                        if (time > player.getTime() && loop)
                            time %= player.getDuration();
                        if (Math.abs(time - player.getTime()) > ACCEPTABLE_SYNC_TIME && Math.abs(time - lastCorrectedTime) > ACCEPTABLE_SYNC_TIME) {
                            lastCorrectedTime = time;
                            player.seekTo(time); // WaterMedia 3.0 (next major update with VLC 4) includes seekFastTo
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void prepare(String url, float volume, float minDistance, float maxDistance, boolean playing, boolean loop, int tick) {
        if (player == null)
            return;
        lock.lock();
        try {
            if (needsUpdate) {
                // fixes random crash, when values are too high it causes a jvm crash, caused weird behavior when game is paused
                GlStateManager._pixelStore(3314, 0);
                GlStateManager._pixelStore(3316, 0);
                GlStateManager._pixelStore(3315, 0);
                RenderSystem.bindTexture(texture);
                if (first) {
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                    first = false;
                } else
                    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                needsUpdate = false;
            }
        } finally {
            lock.unlock();
        }
        
    }
    
    public void free() {
        if (player != null) {
            SyncVideoPlayer tempPlayer = player;
            tempPlayer.release();
        }
        if (texture != -1) {
            GlStateManager._deleteTexture(texture);
            texture = -1;
        }
        player = null;
    }
    
    @Override
    public void release() {
        free();
        synchronized (OPEN_DISPLAYS) {
            OPEN_DISPLAYS.remove(this);
        }
    }
    
    @Override
    public int texture() { return texture; }
    
    @Override
    public void pause(String url, float volume, float minDistance, float maxDistance, boolean playing, boolean loop, int tick) {
        if (player == null) return;
        player.seekTo(tick);
        player.pause();
    }
    
    @Override
    public void resume(String url, float volume, float minDistance, float maxDistance, boolean playing, boolean loop, int tick) {
        if (player == null) return;
        player.seekTo(tick);
        player.play();
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
    
}
