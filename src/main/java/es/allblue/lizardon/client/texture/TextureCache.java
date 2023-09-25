package es.allblue.lizardon.client.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import es.allblue.lizardon.client.display.FrameDisplay;
import es.allblue.lizardon.client.display.FramePictureDisplay;
import es.allblue.lizardon.client.display.FrameVideoDisplay;
import me.lib720.madgag.gif.fmsware.GifDecoder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;



import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import me.srrapero720.watermedia.api.WaterMediaAPI;

import me.srrapero720.watermedia.api.image.ImageFetch;
import me.srrapero720.watermedia.api.image.ImageRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import team.creative.creativecore.common.util.math.vec.Vec3d;

public class TextureCache {

    private static final HashMap<String, TextureCache> cached = new HashMap<>();

    @SubscribeEvent
    public static void render(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (Iterator<TextureCache> iterator = cached.values().iterator(); iterator.hasNext();) {
                TextureCache type = iterator.next();
                if (!type.isUsed()) {
                    type.remove();
                    iterator.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public static void render(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            FrameVideoDisplay.tick();
    }

    public static void reloadAll() {
        for (TextureCache cache : cached.values())
            cache.reload();
    }

    @SubscribeEvent
    public static void unload(WorldEvent.Unload event) {
        if (event.getWorld().isClientSide()) {
            for (TextureCache cache : cached.values())
                cache.remove();
            cached.clear();
            FrameVideoDisplay.unload();
        }
    }

    public static TextureCache get(String url) {
        TextureCache cache = cached.get(url);
        if (cache != null) {
            cache.use();
            return cache;
        }
        cache = new TextureCache(url);
        cached.put(url, cache);
        return cache;
    }

    public final String url;
    private int[] textures;
    private int width;
    private int height;
    private long[] delay;
    private long duration;
    private boolean isVideo;
    private final boolean canSeek;

    private ImageFetch seeker;
    private boolean ready = false;
    private String error;

    private int usage = 0;

    private GifDecoder decoder;
    private int remaining;

    public TextureCache(String url) {
        this.url = url;
        this.canSeek = true;
        use();
        trySeek();
    }

    // Tweak between WaterMedia and LittleFrames
    public TextureCache(BufferedImage image) {
        this.url = null;
        this.canSeek = false;
        process(image);
    }

    // Tweak between WaterMedia and LittleFrames
    public TextureCache(GifDecoder image) {
        this.url = null;
        this.canSeek = false;
        process(image);
    }

    private void trySeek() {
        if (seeker != null || !canSeek || url == null || url.isEmpty())
            return;
        //if (ImageFetch.canSeek()) {
        if (true) {
            seeker = new ImageFetch(url) {
                private final Minecraft MC = Minecraft.getInstance();

                @Override
                public ImageFetch setOnSuccessCallback(TaskSuccessful task) {
                    return super.setOnSuccessCallback(task);
                }

                @Override
                public ImageFetch setOnFailedCallback(TaskFailed task) {

                    return super.setOnFailedCallback(task);
                }

                /*
                @Override
                public void onFailed(@NotNull Exception e) {
                    // This going to be enhanced on next watermedia version using GifLoadingException
                    if (e instanceof IOException && e.getMessage().isEmpty())
                        processFailed("download.exception.gif");
                    else if (e instanceof ImageFetch.NoPictureException) {
                        //if (LittleFrames.CONFIG.useVLC) {
                        if (true) {
                            processVideo();
                            isVideo = true;
                        } else
                            processFailed("No image found");
                    } else if (e.getMessage().startsWith("Server returned HTTP response code: 403"))
                        processFailed("download.exception.forbidden");
                    else if (e.getMessage().startsWith("Server returned HTTP response code: 404"))
                        processFailed("download.exception.notfound");
                    else
                        processFailed("download.exception.invalid");
                }

                @Override
                public void onSuccess(ImageRenderer renderablePicture) {
                    if (renderablePicture.decoder != null) {
                        MC.executeBlocking(() -> process(renderablePicture.decoder));
                    } else if (renderablePicture.image != null) {
                        MC.executeBlocking(() -> process(renderablePicture.image));
                    }
                }*/
            };
        }
    }


    public static int preRender(BufferedImage image, int width, int height) {
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        boolean alpha = false;

        if (image.getColorModel().hasAlpha()) for (int pixel : pixels)
            if ((pixel >> 24 & 0xFF) < 0xFF) {
                alpha = true;
                break;
            }

        int bytesPerPixel = alpha ? 4 : 3;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bytesPerPixel);
        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
            buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green
            buffer.put((byte) (pixel & 0xFF)); // Blue
            if (alpha) buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
        }
        buffer.flip();

        int textureID = GL11.glGenTextures(); //Generate texture ID
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); // Bind texture ID
//        RenderSystem.bindTexture(textureID); // unsafe for other versions - Bind texture ID

        //Setup wrap mode
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        if (!alpha) GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, GL11.GL_ONE);

        // prevents random crash, when values are too high it causes a jvm crash, caused weird behavior when game is paused
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, GL11.GL_ZERO);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, GL11.GL_ZERO);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, GL11.GL_ZERO);

        //Send texel data to OpenGL
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, alpha ? GL11.GL_RGBA8 : GL11.GL_RGB8, width, height, 0, alpha ? GL11.GL_RGBA : GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);

        //Return the texture ID so we can bind it later again
        return textureID;
    }

    private int getTexture(int index) {
        if (textures[index] == -1 && decoder != null) {
            textures[index] = preRender(decoder.getFrame(index), width, height);
            remaining--;
            if (remaining <= 0)
                decoder = null;
        }
        return textures[index];
    }

    public int getTexture(long time) {
        if (textures == null)
            return -1;
        if (textures.length == 1)
            return getTexture(0);
        int last = getTexture(0);
        for (int i = 1; i < delay.length; i++) {
            if (delay[i] > time)
                break;
            last = getTexture(i);
        }
        return last;
    }

    public FrameDisplay createDisplay(Vec3d pos, String url, float volume, float minDistance, float maxDistance, boolean loop) {
        return createDisplay(pos, url, volume, minDistance, maxDistance, loop, false);
    }

    public FrameDisplay createDisplay(Vec3d pos, String url, float volume, float minDistance, float maxDistance, boolean loop, boolean noVideo) {
        volume *= Minecraft.getInstance().options.getSoundSourceVolume(SoundCategory.MASTER);
        if (textures == null && !noVideo)
            //if (textures == null && !noVideo && LittleFrames.CONFIG.useVLC)
            return FrameVideoDisplay.createVideoDisplay(pos, url, volume, minDistance, maxDistance, loop);
        return new FramePictureDisplay(this);
    }

    public String getError() {
        return error;
    }

    public void processVideo() {
        this.textures = null;
        this.error = null;
        this.isVideo = true;
        this.ready = true;
        this.seeker = null;
    }

    public void processFailed(String error) {
        this.textures = null;
        this.error = error;
        this.ready = true;
        this.seeker = null;
    }

    public void process(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        textures = new int[] { preRender(image, width, height) };
        delay = new long[] { 0 };
        duration = 0;
        seeker = null;
        ready = true;
    }

    public void process(GifDecoder decoder) {
        Dimension frameSize = decoder.getFrameSize();
        width = (int) frameSize.getWidth();
        height = (int) frameSize.getHeight();
        textures = new int[decoder.getFrameCount()];
        delay = new long[decoder.getFrameCount()];

        this.decoder = decoder;
        this.remaining = decoder.getFrameCount();
        long time = 0;
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            textures[i] = -1;
            delay[i] = time;
            time += decoder.getDelay(i);
        }
        duration = time;
        seeker = null;
        ready = true;
    }

    public boolean ready() {
        if (ready)
            return true;
        trySeek();
        return false;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void reload() {
        remove();
        error = null;
        trySeek();
    }

    public void use() {
        usage++;
    }

    public void unuse() {
        usage--;
    }

    public boolean isUsed() {
        return usage > 0;
    }

    public void remove() {
        if (!canSeek)
            return; // If can't seek then DO NOT delete texture...
        ready = false;
        if (textures != null)
            for (int i = 0; i < textures.length; i++)
                GlStateManager._deleteTexture(textures[i]);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long[] getDelay() {
        return delay;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isAnimated() {
        return textures.length > 1;
    }

    public int getFrameCount() {
        return textures.length;
    }

}
