package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageVerMisiones;
import es.allblue.lizardon.objects.DatosMision;
import es.allblue.lizardon.objects.DatosNPC;
import es.allblue.lizardon.objects.Mision;
import es.allblue.lizardon.objects.ObjetivoMision;
import es.allblue.lizardon.objects.misiones.MisionesJugador;
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
import noppes.npcs.api.handler.data.IQuestCategory;
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
        NpcAPI npcApi = NpcAPI.Instance();
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

        HashMap<String, Integer> categorias = new HashMap<>();

        for(IQuestCategory category : npcApi.getQuests().categories()){
            categorias.put(category.getName(), category.quests().size());
        }

        MisionesJugador misionesJugador = new MisionesJugador();
        misionesJugador.setMisiones(misiones);
        misionesJugador.setCategorias(categorias);


        Gson gson = new Gson();
        String res = gson.toJson(misionesJugador);
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageVerMisiones(res));
    }

    private void guardarMision(IQuest mActiva, PlayerWrapper wrapper) {

    }
    
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
