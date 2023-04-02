package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageReturn;
import es.allblue.lizardon.objects.Mision;
import es.allblue.lizardon.objects.ObjetivoMision;
import es.allblue.lizardon.objects.SetPC;
import es.allblue.lizardon.objects.Taxi;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.montoyo.mcef.api.IJSQueryCallback;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.util.NBTJsonUtil;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SMessageTaxi implements Runnable{
    private String str;
    private ServerPlayerEntity player;
    private IJSQueryCallback callback;

    public SMessageTaxi(String str){
        this.str = str;
    }

    @Override
    public void run() {
        System.out.println(str);
        Gson gson = new Gson();
        Taxi taxi = gson.fromJson(str, Taxi.class);

        player.teleportTo(taxi.getX(), taxi.getY(), taxi.getZ());
    }

    public static SMessageTaxi decode(PacketBuffer buf) {
        SMessageTaxi message = new SMessageTaxi(buf.toString(Charsets.UTF_8));
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
