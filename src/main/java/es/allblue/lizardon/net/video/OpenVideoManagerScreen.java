package es.allblue.lizardon.net.video;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenVideoManagerScreen implements IMessage<OpenVideoManagerScreen> {

    private BlockPos blockPos;
    private String url;
    private int tick;
    private int volume;
    private boolean loop;

    private int sizeX;

    private int sizeY;

    private int posX;

    private int posY;

    public OpenVideoManagerScreen() {}

    public OpenVideoManagerScreen(BlockPos blockPos, String url, int tick, int volume, boolean loop, int sizeX, int sizeY, int posX, int posY) {
        this.blockPos = blockPos;
        this.url = url;
        this.tick = tick;
        this.volume = volume;
        this.loop = loop;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void encode(OpenVideoManagerScreen message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.blockPos);
        buffer.writeUtf(message.url);
        buffer.writeInt(message.tick);
        buffer.writeInt(message.volume);
        buffer.writeBoolean(message.loop);
        buffer.writeInt(message.sizeX);
        buffer.writeInt(message.sizeY);
        buffer.writeInt(message.posX);
        buffer.writeInt(message.posY);

    }

    @Override
    public OpenVideoManagerScreen decode(PacketBuffer buffer) {
        return new OpenVideoManagerScreen(buffer.readBlockPos(), buffer.readUtf(), buffer.readInt(), buffer.readInt(), buffer.readBoolean(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void handle(OpenVideoManagerScreen message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ClientHandler.openVideoGUI(message.blockPos, message.url, message.tick, message.volume, message.loop, message.sizeX, message.sizeY, message.posX, message.posY);
        });
    }
}