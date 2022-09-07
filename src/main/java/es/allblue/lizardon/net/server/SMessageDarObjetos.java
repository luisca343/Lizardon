package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.init.registry.ItemRegistration;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.ExampleVoicechatPlugin;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.DarObjetos;
import es.allblue.lizardon.objects.DatosLlamada;
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
import java.util.function.Supplier;

public class SMessageDarObjetos implements Runnable{
    private String str;
    private ServerPlayerEntity player;

    public SMessageDarObjetos(String str){
        this.str = str;
    }
    
    @Override
    public void run() {
        Gson gson = new Gson();
        DarObjetos darObjetos = gson.fromJson(str, DarObjetos.class);

        ArrayList<String> objetos = darObjetos.getObjetos();

        HashMap<String, Integer> recompensas = new HashMap<>();
        for (int i = 0; i < objetos.size(); i++) {
            String nombre = objetos.get(i);
            if (!recompensas.containsKey("")) {
                recompensas.put(nombre, 1);
            } else {
                recompensas.put(nombre, recompensas.get(nombre) + 1);
            }
        }
        String mensaje = Lizardon.HEADER_MENSAJE + "§lHas obtenido: §r";
        World world = player.level;
        for (Map.Entry me : recompensas.entrySet()) {
            String nombre = (String) me.getKey();
            int cantidad = (int) me.getValue();
            String[] partes = nombre.split(":");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(partes[0], partes[1]));
            ItemStack stack = new ItemStack(item, cantidad);

            ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), stack);
            itemEntity.setPickUpDelay(0);

            mensaje += "§2§l" + stack.getDisplayName().getContents() + "§6x" + cantidad + "§f,";
            world.addFreshEntity(itemEntity);
        }
        StringTextComponent msg = new StringTextComponent(mensaje.substring(0, mensaje.length() - 1));
        player.sendMessage(msg, ChatType.SYSTEM, Util.NIL_UUID);
    }

    public static SMessageDarObjetos decode(PacketBuffer buf) {
        SMessageDarObjetos message = new SMessageDarObjetos(buf.toString(Charsets.UTF_8));
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
