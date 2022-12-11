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

public class CMessageVerMisiones implements Runnable{
    private String str;
    private ServerPlayerEntity player;
    Map<String, DatosNPC> datosNpc;

    public CMessageVerMisiones(String str){
        this.str = str;
    }

    @Override
    public void run() {
        String nombreArchivo = "npcs.json";
        File fileDatos = new File(nombreArchivo);
        try{
        if(!fileDatos.exists()) {
            fileDatos.createNewFile();
        }
            Gson gson = new Gson();

            Reader reader = Files.newBufferedReader(Lizardon.PROXY.getRuta(nombreArchivo));
            datosNpc = gson.fromJson(reader, new TypeToken<Map<String, DatosNPC>>(){}.getType());

            ArrayList<Mision> misiones = gson.fromJson(str, new TypeToken<ArrayList<Mision>>(){}.getType());

            ArrayList<Mision> misionesNuevas = new ArrayList<>();
            actualizar(misiones, misionesNuevas);

            Collections.sort(misionesNuevas, comparing(Mision::getId));
            String res = gson.toJson(misionesNuevas);
            ClientProxy.callbackMisiones.success(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CMessageVerMisiones decode(PacketBuffer buf) {
        CMessageVerMisiones message = new CMessageVerMisiones(buf.toString(Charsets.UTF_8));
        return message;
    }

    public void actualizar(ArrayList<Mision> listaAnterior, ArrayList<Mision> lista){
        for (Mision mision : listaAnterior) {
            if(datosNpc.containsKey(mision.getNombreNPC())){
                mision.setSkin(datosNpc.get(mision.getNombreNPC()).getSkin());
            }
            lista.add(mision);
        }
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
