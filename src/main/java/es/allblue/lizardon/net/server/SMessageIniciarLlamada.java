package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.ExampleVoicechatPlugin;
import es.allblue.lizardon.objects.DatosLlamada;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SMessageIniciarLlamada implements Runnable{
    private String str;
    private ServerPlayerEntity player;

    public SMessageIniciarLlamada(String str){
        this.str = str;
    }
    
    @Override
    public void run() {
        VoicechatServerApi SERVER_API = ExampleVoicechatPlugin.SERVER_API;
        Gson gson = new Gson();
        DatosLlamada datosLlamada = gson.fromJson(str, DatosLlamada.class);
        Group group = SERVER_API.createGroup(datosLlamada.getIdLlamada(), null);
        VoicechatConnection conn = SERVER_API.getConnectionOf(player.getUUID());
        conn.setGroup(group);
    }

    public static SMessageIniciarLlamada decode(PacketBuffer buf) {
        SMessageIniciarLlamada message = new SMessageIniciarLlamada(buf.toString(Charsets.UTF_8));
        return message;
    }

    public void encode(PacketBuffer buf) {
        buf.writeCharSequence(str, Charsets.UTF_8);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        player = contextSupplier.get().getSender();
        contextSupplier.get().enqueueWork((Runnable) this);
        contextSupplier.get().setPacketHandled(true);
    }
}
