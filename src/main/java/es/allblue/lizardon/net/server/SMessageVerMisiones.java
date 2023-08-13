package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageVerMisiones;
import es.allblue.lizardon.objects.DatosMision;
import es.allblue.lizardon.objects.DatosNPC;
import es.allblue.lizardon.objects.Mision;
import es.allblue.lizardon.objects.ObjetivoMision;
import es.allblue.lizardon.util.FileHelper;
import io.leangen.geantyref.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.montoyo.mcef.api.IJSQueryCallback;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.entity.data.IPixelmonPlayerData;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.wrapper.PlayerWrapper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SMessageVerMisiones implements Runnable{
    private String str;
    private ServerPlayerEntity player;
    private IJSQueryCallback callback;

    Map<String, DatosNPC> datosNpc;
    Map<Integer, DatosMision> datosMisionMap;

    public SMessageVerMisiones(String str){
        this.str = str;
    }

    @Override
    public void run(){
        datosMisionMap = ( Map<Integer, DatosMision>) FileHelper.readFile("config/lizardon/misiones.json", new TypeToken< Map<Integer, DatosMision>>(){}.getType());
        if(datosMisionMap == null){
            datosMisionMap = new HashMap<>();
        }

        PlayerWrapper wrapper = new PlayerWrapper(player);
        IQuest[] mActivas = wrapper.getActiveQuests();
        IQuest[] mCompletadas = wrapper.getFinishedQuests();
        ArrayList<DatosMision> misiones = new ArrayList<>();

        for(IQuest mActiva : mActivas){
            DatosMision datos = datosMisionMap.get(mActiva.getId());
            datos.setActiva(true);
            datos.setEstado("Activa");

            IQuestObjective[] objetivos = mActiva.getObjectives(wrapper);
            ArrayList<ObjetivoMision> objetivosMision = new ArrayList<>();
            if (objetivos.length > 0 ){
                for (IQuestObjective objetivo : objetivos) {
                    ObjetivoMision objetivoMision = new ObjetivoMision();
                    objetivoMision.setNombre(objetivo.getText());
                    objetivoMision.setProgreso(objetivo.getProgress());
                    objetivoMision.setTotal(objetivo.getMaxProgress());
                    objetivosMision.add(objetivoMision);
                }
            }

            datos.setObjetivos(objetivosMision);
            misiones.add(datos);
        }

        for(IQuest mCompleta : mCompletadas){
            DatosMision datos = datosMisionMap.get(mCompleta.getId());
            datos.setActiva(false);
            datos.setEstado("Completa");

            misiones.add(datos);
        }

        Gson gson = new Gson();
        String res = gson.toJson(misiones);
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageVerMisiones(res));
    }

    private void guardarMision(IQuest mActiva, PlayerWrapper wrapper) {

    }

/*
    public void runOld() {
        datosNpc = (Map<String, DatosNPC>) FileHelper.readFile("config/lizardon/npcs.json", new TypeToken<Map<String, DatosNPC>>(){}.getType());
        if(datosNpc == null){
            datosNpc = new HashMap<>();
        }
        PlayerWrapper wrapper = new PlayerWrapper(player);
        

        IQuest[] mActivas = wrapper.getActiveQuests();
        IQuest[] mCompletadas = wrapper.getFinishedQuests();

        ArrayList<Integer> idActivas = new ArrayList<>();
        for (IQuest mActiva : mActivas) {
            idActivas.add(mActiva.getId());
        }

        ArrayList<Mision> misiones = new ArrayList<>();

        for (IQuest mActiva : mActivas) {
            guardarMision(misiones, mActiva, wrapper, idActivas);
        }

        for (IQuest mCompleta : mCompletadas) {
            guardarMision(misiones, mCompleta, wrapper, idActivas);
        }

        Gson gson = new Gson();
        String res = gson.toJson(misiones);
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageVerMisiones(res));
    }


    public void guardarMisionOld(ArrayList<Mision> lista, IQuest quest, PlayerWrapper wrapper, ArrayList<Integer> idActivas){
        Mision mision = new Mision();
        mision.setCategoria(quest.getCategory().getName());
        mision.setNombre(quest.getName());
        mision.setTipo(quest.getType());
        mision.setTextoCompletar(quest.getCompleteText());
        mision.setTextoLog(quest.getLogText());
        mision.setRepetible(quest.getIsRepeatable());
        mision.setId(quest.getId());


        Map<String, String> datosQuest = (Map<String, String>) FileHelper.readFile("config/lizardon/quests.json", new TypeToken<Map<String, String>>(){}.getType());
        if(datosQuest == null){
            datosQuest = new HashMap<>();
        }

        String npc = datosQuest.get(quest.getName());
        DatosNPC datos = datosNpc.get(npc);

        mision.setSkin(datos.getSkin());
        mision.setNombreNPC(npc);

        mision.setX(datos.getX());
        mision.setY(datos.getY());
        mision.setZ(datos.getZ());

        if(idActivas.contains(quest.getId())){
            mision.setEstado("Activa");
            IQuestObjective[] objetivos = quest.getObjectives(wrapper);
            ArrayList<ObjetivoMision> objetivosMision = new ArrayList<>();
            if (objetivos.length > 0 ){
                for (IQuestObjective objetivo : objetivos) {
                    ObjetivoMision objetivoMision = new ObjetivoMision();
                    objetivoMision.setNombre(objetivo.getText());
                    objetivoMision.setProgreso(objetivo.getProgress());
                    objetivoMision.setTotal(objetivo.getMaxProgress());
                    objetivosMision.add(objetivoMision);
                }
            }
            mision.setObjetivos(objetivosMision);

        }else {
            mision.setEstado("Completa");
        }
        lista.add(mision);
    }*/

    public static SMessageVerMisiones decode(PacketBuffer buf) {
        SMessageVerMisiones message = new SMessageVerMisiones(buf.toString(Charsets.UTF_8));
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
