package es.allblue.lizardon.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import es.allblue.lizardon.Lizardon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.StringTextComponent;
import nick1st.fancyvideo.api.MediaPlayers;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.TitleDescription;

public class VideoScreen extends ContainerScreen<Container> {

    boolean init = false;
    boolean stopped = true;

    int playerId;
    MediaPlayer mediaPlayer;

    public VideoScreen(String url) {
        super(new DummyContainer(), Minecraft.getInstance().player != null ? Minecraft.getInstance().player.inventory : null, new StringTextComponent(""));
        int playerId = nick1st.fancyvideo.api.MediaPlayer.getNew();
        mediaPlayer = MediaPlayers.getPlayer(playerId).getTrueMediaPlayer().mediaPlayer();
        mediaPlayer.media().prepare(url);
        mediaPlayer.events().addMediaEventListener(new MediaEventAdapter() {
            @Override
            public void mediaSubItemAdded(Media media, MediaRef newChild) {
                Lizardon.LOGGER.info("item added");
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                for (String mrl : mediaList.media().mrls()) {
                    Lizardon.LOGGER.info("mrl=" + mrl);
                }
                mediaList.release();
            }

            @Override
            public void mediaSubItemTreeAdded(Media media, MediaRef item) {
                Lizardon.LOGGER.info("item tree added");
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                for (String mrl : mediaList.media().mrls()) {
                    Lizardon.LOGGER.info("mrl=" + mrl);
                }
                mediaList.release();
            }
        });


        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventListener() {
            @Override
            public void mediaChanged(MediaPlayer mediaPlayer, MediaRef mediaRef) {}
            @Override
            public void opening(MediaPlayer mediaPlayer) {}
            @Override
            public void buffering(MediaPlayer mediaPlayer, float v) {}
            @Override
            public void playing(MediaPlayer mediaPlayer) {}
            @Override
            public void paused(MediaPlayer mediaPlayer) {}
            @Override
            public void stopped(MediaPlayer mediaPlayer) { if (!stopped) onClose(); }
            @Override
            public void forward(MediaPlayer mediaPlayer) {}
            @Override
            public void backward(MediaPlayer mediaPlayer) {}
            @Override
            public void finished(MediaPlayer mediaPlayer) {}
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long l) {}

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float v) {

            }

            @Override
            public void seekableChanged(MediaPlayer mediaPlayer, int i) {}
            @Override
            public void pausableChanged(MediaPlayer mediaPlayer, int i) {}
            @Override
            public void titleListChanged(MediaPlayer mediaPlayer) {}

            @Override
            public void titleSelectionChanged(MediaPlayer mediaPlayer, TitleDescription titleDescription, int i) {

            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String s) {}
            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long l) {}
            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int i) {}

            @Override
            public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType trackType, int i, String s) {

            }

            @Override
            public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType trackType, int i, String s) {

            }

            @Override
            public void elementaryStreamUpdated(MediaPlayer mediaPlayer, TrackType trackType, int i, String s) {

            }

            @Override
            public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType trackType, String s, String s1) {

            }

            @Override
            public void corked(MediaPlayer mediaPlayer, boolean b) {}
            @Override
            public void muted(MediaPlayer mediaPlayer, boolean b) {}
            @Override
            public void volumeChanged(MediaPlayer mediaPlayer, float v) {}
            @Override
            public void audioDeviceChanged(MediaPlayer mediaPlayer, String s) {}
            @Override
            public void chapterChanged(MediaPlayer mediaPlayer, int i) {}
            @Override
            public void programAdded(MediaPlayer mediaPlayer, int i) {}
            @Override
            public void programDeleted(MediaPlayer mediaPlayer, int i) {}
            @Override
            public void programUpdated(MediaPlayer mediaPlayer, int i) {}
            @Override
            public void programSelected(MediaPlayer mediaPlayer, int i, int i1) {}
            @Override
            public void error(MediaPlayer mediaPlayer) {}
            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {}
        });
        mediaPlayer.audio().setVolume(200);
    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        if (MediaPlayers.getPlayer(playerId) != null) {
            if (!init) {
                mediaPlayer.controls().play();
                init = true;
                stopped = false;
            }
            // Generic Render Code for Screens
            int width = Minecraft.getInstance().screen.width;
            int height = Minecraft.getInstance().screen.height;

            MediaPlayers.getPlayer(playerId).bindFrame();

            RenderSystem.enableBlend();
            RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
            AbstractGui.blit(pMatrixStack, 0, 0, 0.0F, 0.0F, width, height, width, height);
        } else {
            // Generic Render Code for Screens
            int width = Minecraft.getInstance().screen.width;
            int height = Minecraft.getInstance().screen.height;

            int width2;

            if (width <= height) {
                width2 = width / 3;
            } else {
                width2 = height / 2;
            }

            //Minecraft.getInstance().textureManager.bind(new DynamicResourceLocation(Lizardon.MOD_ID, "fallback"));

            RenderSystem.enableBlend();
            RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
            AbstractGui.blit(pMatrixStack, 0, 0, 0.0F, 0.0F, width, height, width2, width2);
        }
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {}

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (hasShiftDown() && pKeyCode == 256) {
            this.onClose();
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        if (!stopped) {
            stopped = true;
            Minecraft.getInstance().getSoundManager().resume();
            mediaPlayer.controls().stop();
            super.onClose();
        }
    }
}
