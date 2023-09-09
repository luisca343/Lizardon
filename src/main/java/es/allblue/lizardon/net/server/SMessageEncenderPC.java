package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PixelmonStorageManager;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreenPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientChangeOpenPCPacket;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.DarObjetos;
import es.allblue.lizardon.util.MessageUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class SMessageEncenderPC implements Runnable{
    private String str;
    private ServerPlayerEntity player;

    public SMessageEncenderPC(String str){
        this.str = str;
    }
    
    @Override
    public void run() {
        try{
            UUID uuid = UUID.fromString(str);
            NetworkHelper.sendPacket(new ClientChangeOpenPCPacket(uuid), player);
            if(!player.getPersistentData().getBoolean("frentebatalla")){
                OpenScreenPacket.open(player, EnumGuiScreen.PC, new int[0]);
            } else {
                MessageUtil.enviarMensaje(player, "No puedes abrir el PC en el Frente Batalla");
            }
        }catch(Exception e){
            player.sendMessage(new StringTextComponent("Ha ocurrido un error"), UUID.randomUUID());
        }
    }

    public static SMessageEncenderPC decode(PacketBuffer buf) {
        SMessageEncenderPC message = new SMessageEncenderPC(buf.toString(Charsets.UTF_8));
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
