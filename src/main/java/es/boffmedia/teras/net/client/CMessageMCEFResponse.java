package es.boffmedia.teras.net.client;


import com.google.common.base.Charsets;
import es.boffmedia.teras.client.ClientProxy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CMessageMCEFResponse implements Runnable{
    private String json;
    private ServerPlayerEntity player;

    public CMessageMCEFResponse(String json){
        this.json = json;
    }

    @Override
    public void run() {
        ClientProxy.callbackMCEF.success(json);
    }

    public static CMessageMCEFResponse decode(PacketBuffer buf) {
        CMessageMCEFResponse message = new CMessageMCEFResponse(buf.toString(Charsets.UTF_8));
        return message;
    }

    public void encode(PacketBuffer buf) {
        buf.writeCharSequence(json, Charsets.UTF_8);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        player = contextSupplier.get().getSender();
        contextSupplier.get().enqueueWork((Runnable) this);
        contextSupplier.get().setPacketHandled(true);
    }
}
