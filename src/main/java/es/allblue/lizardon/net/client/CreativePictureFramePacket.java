package es.allblue.lizardon.net.client;

import de.maxhenkel.voicechat.api.ServerPlayer;
import es.allblue.lizardon.blocks.PictureFrame;
import es.allblue.lizardon.tileentity.PictureFrameTE;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import team.creative.creativecore.common.network.CreativePacket;


public class CreativePictureFramePacket extends CreativePacket {
    
    public BlockPos pos;
    public boolean playing;
    public int tick;
    
    public CreativePictureFramePacket() {}

    public CreativePictureFramePacket(BlockPos pos, boolean playing, int tick) {
        this.pos = pos;
        this.playing = playing;
        this.tick = tick;
    }
    
    @Override
    public void executeClient(PlayerEntity player) {
        TileEntity be = player.level.getBlockEntity(pos);
        if (be instanceof PictureFrameTE) {
            PictureFrameTE frame = (PictureFrameTE) be;
            frame.playing = playing;
            frame.tick = tick;
            if (frame.display != null) {
                if (playing)
                    frame.display.resume(frame.getURL(), frame.volume, frame.minDistance, frame.maxDistance, frame.playing, frame.loop, frame.tick);
                else
                    frame.display.pause(frame.getURL(), frame.volume, frame.minDistance, frame.maxDistance, frame.playing, frame.loop, frame.tick);
            }
        }
    }

    @Override
    public void executeServer(PlayerEntity playerEntity) {

    }

}
