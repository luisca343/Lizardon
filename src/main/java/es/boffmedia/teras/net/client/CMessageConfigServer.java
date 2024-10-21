package es.boffmedia.teras.net.client;


import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.util.objects._old.serverdata.TerasConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CMessageConfigServer implements Runnable{
    private String datos;
    private ServerPlayerEntity player;

    public CMessageConfigServer(String str){
        this.datos = str;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        TerasConfig conf = gson.fromJson(datos, TerasConfig.class);
        Teras.config = conf;
    }

    public static CMessageConfigServer decode(PacketBuffer buf) {
        CMessageConfigServer message = new CMessageConfigServer(buf.toString(Charsets.UTF_8));
        return message;
    }

    public void encode(PacketBuffer buf) {
        buf.writeCharSequence(datos, Charsets.UTF_8);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        player = contextSupplier.get().getSender();
        contextSupplier.get().enqueueWork((Runnable) this);
        contextSupplier.get().setPacketHandled(true);
    }
}
