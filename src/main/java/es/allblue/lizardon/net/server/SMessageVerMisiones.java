package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.event.ModEvents;
import es.allblue.lizardon.objects.DarObjetos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.wrapper.PlayerWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SMessageVerMisiones implements Runnable{
    private String str;
    private ServerPlayerEntity player;

    public SMessageVerMisiones(String str){
        this.str = str;
    }
    
    @Override
    public void run() {
        NpcAPI api = NpcAPI.Instance();

        PlayerWrapper wrapper = new PlayerWrapper(player);
        IQuestObjective obyectivo = api.getQuests().get(3).getObjectives(wrapper)[0];
        obyectivo.setProgress(obyectivo.getProgress()+1);
        Lizardon.getLogger().info("HAS MATADO A " + obyectivo.getProgress()+" / "+obyectivo.getMaxProgress() + " " + obyectivo.getText().split(":")[0]);



        /*
        for ( IQuestCategory category : api.getQuests().categories()) {
            Lizardon.getLogger().info(category.getName());
            for ( IQuest quest : category.quests()) {
                Lizardon.getLogger().info(quest.getName());
            }
        }


        PlayerWrapper wrapper = new PlayerWrapper(player);

        Lizardon.getLogger().info("ACTIVAS: ");
        for (IQuest quest:wrapper.getActiveQuests()) {
            Lizardon.getLogger().info(quest.getName());
        }
        Lizardon.getLogger().info("FINALIZADAS: ");
        for (IQuest quest:wrapper.getFinishedQuests()) {
            Lizardon.getLogger().info(quest.getName());
        }*/
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
