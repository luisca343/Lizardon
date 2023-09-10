package es.allblue.lizardon.net.client;


import com.google.common.base.Charsets;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.objects.misiones.DatosNPC;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Comparator.comparing;

public class CMessageGetPC implements Runnable{
    private String str;
    private ServerPlayerEntity player;
    Map<String, DatosNPC> datosNpc;

    public CMessageGetPC(String str){
        this.str = str;
    }

    @Override
    public void run() {
        System.out.println(str);
        ClientProxy.callbackMCEF.success(str);
    }

    public static CMessageGetPC decode(PacketBuffer buf) {
        CMessageGetPC message = new CMessageGetPC(buf.toString(Charsets.UTF_8));
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
