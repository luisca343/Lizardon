package es.allblue.lizardon.net.client;


import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.objects.DatosNPC;
import es.allblue.lizardon.objects.Mision;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.montoyo.mcef.api.IJSQueryCallback;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.wrapper.PlayerWrapper;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CMessageVerMisiones implements Runnable{
    private String str;
    private ServerPlayerEntity player;

    public CMessageVerMisiones(String str){
        this.str = str;
    }

    @Override
    public void run() {
        String nombreArchivo = "Lizardon/npcs.json";
        File fileDatos = new File(nombreArchivo);
        try{
        if(!fileDatos.exists()) {
            fileDatos.createNewFile();
        }
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(nombreArchivo));
            Map<String, DatosNPC> datosNpc = gson.fromJson(reader, new TypeToken<Map<String, DatosNPC>>(){}.getType());

            Map<String, Map<Integer, Mision>> misiones = gson.fromJson(str, new TypeToken<Map<String, Map<Integer,Mision>>>(){}.getType());
            Map<Integer, Mision> activas = misiones.get("Activas");
            Map<Integer, Mision> completas = misiones.get("Completas");

            for(Map.Entry<Integer, Mision> entrada: activas.entrySet()){
                Mision mision = entrada.getValue();
                if(datosNpc.containsKey(mision.getNombreNPC())){
                    mision.setSkin(datosNpc.get(mision.getNombreNPC()).getSkin());
                }
                activas.replace(entrada.getKey(), mision);
            }

            for(Map.Entry<Integer, Mision> entrada: completas.entrySet()){
                Mision mision = entrada.getValue();
                if(datosNpc.containsKey(mision.getNombreNPC())){
                    mision.setSkin(datosNpc.get(mision.getNombreNPC()).getSkin());
                }
                completas.replace(entrada.getKey(), mision);
            }


            Map<String, Map<Integer, Mision>> misiones2 = new HashMap<>();

            misiones2.put("Activas", activas);
            misiones2.put("Completas", completas);

            String res = gson.toJson(misiones2);
            ClientProxy.callbackMisiones.success(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static CMessageVerMisiones decode(PacketBuffer buf) {
        CMessageVerMisiones message = new CMessageVerMisiones(buf.toString(Charsets.UTF_8));
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
