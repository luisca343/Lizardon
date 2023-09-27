package es.allblue.lizardon.client;


import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.gui.TVVideoScreen;
import es.allblue.lizardon.client.gui.VideoScreen;
import es.allblue.lizardon.tileentity.TVBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
public class ClientHandler {

    public static void openVideo(String url, int volume) {
        Minecraft.getInstance().setScreen(new VideoScreen(url, volume));
    }

    public static void manageVideo(BlockPos pos, boolean playing, int tick, int sizeX, int sizeY, int posX, int posY, int canal, String url) {
        TileEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
        if (be instanceof TVBlockEntity) {
            TVBlockEntity tv = (TVBlockEntity) be;
            tv.setPlaying(playing);
            tv.setTick(tick);
            tv.setSizeX(sizeX);
            tv.setSizeY(sizeY);
            tv.setPosX(posX);
            tv.setPosY(posY);
            tv.setCanal(canal);
            tv.setUrl(url);
            tv.updateAABB();
            if (tv.requestDisplay() != null) {
                if (playing)
                    tv.requestDisplay().resume(tv.getUrl(), tv.getVolume(), tv.minDistance, tv.maxDistance, tv.isPlaying(), tv.isLoop(), tick, sizeX, sizeY);
                else
                    tv.requestDisplay().pause(tv.getUrl(), tv.getVolume(), tv.minDistance, tv.maxDistance, tv.isPlaying(), tv.isLoop(), tick, sizeX, sizeY);
            }
        }
    }

    public static void openVideoGUI(BlockPos pos, String url, int tick, int volume, boolean loop, int sizeX, int sizeY, int posX, int posY, int canal, boolean permisos) {
        Lizardon.LOGGER.info("ClientHandler.openVideoGUI: " + pos + " " + url + " " + tick + " " + volume + " " + loop + " " + sizeX + " " + sizeY + " " + posX + " " + posY + " " + canal + " " + permisos);
        TileEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
        if (be instanceof TVBlockEntity) {
            TVBlockEntity tv = (TVBlockEntity) be;
            tv.setUrl(url);
            tv.setTick(tick);
            tv.setVolume(volume);
            tv.setLoop(loop);
            Minecraft.getInstance().setScreen(new TVVideoScreen(be, url, volume, sizeX, sizeY, posX, posY, canal, permisos));
        }
    }

}