package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.LizardonVoicechatPlugin;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageDatosServer;
import es.allblue.lizardon.net.client.CMessageVerMisiones;
import es.allblue.lizardon.objects.DatosServer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Supplier;

public class SMessageDatosServer implements Runnable{
    private String str;
    private ServerPlayerEntity player;

    public SMessageDatosServer(String str){
        this.str = str;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        File file = new File("config/lizardon.json");

        Reader reader = null;
        DatosServer datos;
        try {
            if(!file.exists()){
                file.createNewFile();
                DatosServer nuevosDatos = new DatosServer();
                nuevosDatos.setId(RandomStringUtils.random(8, true, true));
                BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charsets.UTF_8);
                gson.toJson(nuevosDatos, writer);
                writer.flush();
                writer.close();
                datos = nuevosDatos;
            } else{
                reader = Files.newBufferedReader(Paths.get("config/lizardon.json"));
                datos = gson.fromJson(reader, DatosServer.class);
            }

            Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageDatosServer(datos.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SMessageDatosServer decode(PacketBuffer buf) {
        SMessageDatosServer message = new SMessageDatosServer(buf.toString(Charsets.UTF_8));
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
