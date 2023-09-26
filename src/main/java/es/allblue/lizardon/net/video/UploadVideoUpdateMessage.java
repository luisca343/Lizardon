package es.allblue.lizardon.net.video;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.tileentity.TVBlockEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class UploadVideoUpdateMessage implements IMessage<UploadVideoUpdateMessage> {

    private BlockPos blockPos;
    private String url;
    private int volume;
    private boolean loop;
    private boolean isPlaying;
    private boolean reset;

    private int x;

    private int y;

    private int posX;

    private int posY;

    public UploadVideoUpdateMessage() {}

    public UploadVideoUpdateMessage(BlockPos blockPos, String url, int volume, boolean loop, boolean isPlaying, boolean reset) {
        this.blockPos = blockPos;
        this.url = url;
        this.volume = volume;
        this.loop = loop;
        this.isPlaying = isPlaying;
        this.reset = reset;
    }

    public UploadVideoUpdateMessage(BlockPos blockPos, String url, int volume, boolean loop, boolean isPlaying, boolean reset, int tempX, int tempY, int posX, int posY) {
        this.blockPos = blockPos;
        this.url = url;
        this.volume = volume;
        this.loop = loop;
        this.isPlaying = isPlaying;
        this.reset = reset;
        this.x = tempX;
        this.y = tempY;

        this.posX = posX;
        this.posY = posY;


    }


    @Override
    public void encode(UploadVideoUpdateMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.blockPos);
        buffer.writeUtf(message.url);
        buffer.writeInt(message.volume);
        buffer.writeBoolean(message.loop);
        buffer.writeBoolean(message.isPlaying);
        buffer.writeBoolean(message.reset);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.posX);
        buffer.writeInt(message.posY);

    }

    @Override
    public UploadVideoUpdateMessage decode(PacketBuffer buffer) {
        return new UploadVideoUpdateMessage(buffer.readBlockPos(), buffer.readUtf(), buffer.readInt(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void handle(UploadVideoUpdateMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            if (player.level.getBlockEntity(message.blockPos) instanceof TVBlockEntity) {
                TVBlockEntity tvBlockEntity = (TVBlockEntity) player.level.getBlockEntity(message.blockPos);
                if (tvBlockEntity == null) return;

                tvBlockEntity.setBeingUsed(new UUID(0, 0));
                if (message.volume == -1) // NO UPDATE
                    return;

                tvBlockEntity.setUrl(message.url);
                tvBlockEntity.setVolume(message.volume);
                tvBlockEntity.setLoop(message.loop);
                tvBlockEntity.setPlaying(message.isPlaying);
                tvBlockEntity.setSizeX(message.x);
                tvBlockEntity.setSizeY(message.y);
                tvBlockEntity.setPosX(message.posX);
                tvBlockEntity.setPosY(message.posY);

                tvBlockEntity.notifyPlayer();

                if (message.reset)
                    tvBlockEntity.setTick(0);
            }
        });
    }
}