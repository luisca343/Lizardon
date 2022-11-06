package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageVerMisiones;
import es.allblue.lizardon.objects.DatosNPC;
import es.allblue.lizardon.objects.Mision;
import es.allblue.lizardon.objects.RecompensaMision;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.montoyo.mcef.api.IJSQueryCallback;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NPCSpawning;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.data.IPixelmonPlayerData;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.entity.EntityCustomNpc;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SMessageVerMisiones implements Runnable{
    private String str;
    private ServerPlayerEntity player;
    private IJSQueryCallback callback;

    public SMessageVerMisiones(String str){
        this.str = str;
    }

    @Override
    public void run() {
        NpcAPI api = NpcAPI.Instance();
        PlayerWrapper wrapper = new PlayerWrapper(player);
        IQuestHandler questHandler = api.getQuests();


        IQuest[] mActivas = wrapper.getActiveQuests();
        IQuest[] mCompletadas = wrapper.getFinishedQuests();

        Map<Integer, Mision> misionesActivas = new HashMap<>();
        Map<Integer, Mision> misionesCompletas = new HashMap<>();

        for (IQuest mActiva : mActivas) {
            guardarMision(misionesActivas, mActiva);
        }

        for (IQuest mCompleta : mCompletadas) {
            guardarMision(misionesCompletas, mCompleta);
        }

        Map<String, Map<Integer, Mision>> misiones = new HashMap<>();
        misiones.put("Activas", misionesActivas);
        misiones.put("Completas", misionesCompletas);

        Gson gson = new Gson();
        String res = gson.toJson(misiones);
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageVerMisiones(res));
    }

    public void guardarMision(Map<Integer, Mision> lista, IQuest quest){
        Mision mision = new Mision();
        mision.setCategoria(quest.getCategory().getName());
        mision.setNombre(quest.getName());
        mision.setTipo(quest.getType());
        mision.setTextoCompletar(quest.getCompleteText());
        mision.setTextoLog(quest.getLogText());
        mision.setNombreNPC(quest.getNpcName());
        mision.setRepetible(quest.getIsRepeatable());
        mision.setId(quest.getId());
        lista.put(quest.getId(), mision);
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
