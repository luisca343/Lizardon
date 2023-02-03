package es.allblue.lizardon.net.client;


import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.objects.DatosNPC;
import es.allblue.lizardon.objects.Mision;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
