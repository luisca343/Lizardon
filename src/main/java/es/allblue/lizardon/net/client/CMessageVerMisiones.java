package es.allblue.lizardon.net.client;


import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.objects.DatosNPC;
import es.allblue.lizardon.objects.Mision;
import es.allblue.lizardon.util.FileHelper;
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
        datosNpc = (Map<String, DatosNPC>) FileHelper.readFile("config/lizardon/npcs.json", new io.leangen.geantyref.TypeToken<Map<String, DatosNPC>>(){}.getType());

        Gson gson = new Gson();
        ArrayList<Mision> misiones = gson.fromJson(str, new TypeToken<ArrayList<Mision>>(){}.getType());
        ArrayList<Mision> misionesNuevas = new ArrayList<>();
        //actualizar(misiones, misionesNuevas);

        Collections.sort(misiones, comparing(Mision::getId));
        String res = gson.toJson(misiones);
        ClientProxy.callbackMisiones.success(res);

    }

    public static CMessageVerMisiones decode(PacketBuffer buf) {
        CMessageVerMisiones message = new CMessageVerMisiones(buf.toString(Charsets.UTF_8));
        return message;
    }

    public void actualizar(ArrayList<Mision> listaAnterior, ArrayList<Mision> lista){
        for (Mision mision : listaAnterior) {
            if(datosNpc.containsKey(mision.getNombreNPC())){
                mision.setSkin(datosNpc.get(mision.getNombreNPC()).getSkin());
                mision.setX(datosNpc.get(mision.getNombreNPC()).getX());
                mision.setY(datosNpc.get(mision.getNombreNPC()).getY());
                mision.setZ(datosNpc.get(mision.getNombreNPC()).getZ());
            }
            System.out.println(mision.getNombreNPC());
            System.out.println(mision.getSkin());
            System.out.println(mision.getX());
            System.out.println(mision.getY());
            System.out.println(mision.getZ());
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
