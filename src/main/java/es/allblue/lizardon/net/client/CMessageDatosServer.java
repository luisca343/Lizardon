package es.allblue.lizardon.net.client;


import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.objects.DatosNPC;
import es.allblue.lizardon.objects.GetUserData;
import es.allblue.lizardon.objects.Mision;
import es.allblue.lizardon.objects.UserData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CMessageDatosServer implements Runnable{
    private String idServer;
    private ServerPlayerEntity player;

    public CMessageDatosServer(String str){
        this.idServer = str;
    }

    @Override
    public void run() {
        Lizardon.PROXY.setIdServidor(idServer);
        Gson gson = new Gson();
        String uuid = Minecraft.getInstance().player.getStringUUID();
        String nombre = Minecraft.getInstance().player.getName().getString();

        UserData data = new UserData(uuid, nombre, idServer);


        GetUserData userData = new GetUserData();
        userData.setMundo(idServer);
        userData.setUuid(uuid);
        userData.setNombre(nombre);

        String respuesta = gson.toJson(userData);
        System.out.println("RECIBIDA RESPUESTA: " + respuesta);

        ClientProxy.callbackMisiones.success(respuesta);

        // Hacer el sistema de necesitar medallas para hacer misiones
        // Y el sistema de misiones que se activen en días concretos
    }

    public static CMessageDatosServer decode(PacketBuffer buf) {
        CMessageDatosServer message = new CMessageDatosServer(buf.toString(Charsets.UTF_8));
        return message;
    }

    public void encode(PacketBuffer buf) {
        buf.writeCharSequence(idServer, Charsets.UTF_8);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        player = contextSupplier.get().getSender();
        contextSupplier.get().enqueueWork((Runnable) this);
        contextSupplier.get().setPacketHandled(true);
    }
}
