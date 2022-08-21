package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.ExampleVoicechatPlugin;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.DatosLlamada;
import es.allblue.lizardon.objects.QueryUser;
import es.allblue.lizardon.objects.UserData;
import init.ItemInit;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class SMessageTest implements Runnable{
    private String str;
    private ServerPlayerEntity player;

    public SMessageTest(String str){
        this.str = str;
    }
    
    @Override
    public void run() {
        System.out.println("ENTRANDO AL RUN");
        VoicechatServerApi SERVER_API = ExampleVoicechatPlugin.SERVER_API;

        Gson gson = new Gson();
        System.out.println(str);
        DatosLlamada datosLlamada = gson.fromJson(str, DatosLlamada.class);

        System.out.println("Creando grupo "+datosLlamada.getIdLlamada());
        Group group = SERVER_API.createGroup(datosLlamada.getIdLlamada(), null);
        VoicechatConnection conn = SERVER_API.getConnectionOf(player.getUUID());
        conn.setGroup(group);
        System.out.println("Uniendo al grupo "+datosLlamada.getIdLlamada());
    }

    public static SMessageTest decode(PacketBuffer buf) {
        SMessageTest message = new SMessageTest(buf.toString(Charsets.UTF_8));
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
