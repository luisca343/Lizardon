package es.allblue.lizardon.net.server;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.objects.mina.DarCaja;
import es.allblue.lizardon.objects.ObjetoMC;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SMessageDarCaja implements Runnable{
    private String str;
    private ServerPlayerEntity player;

    public SMessageDarCaja(String str){
        this.str = str;
    }
    
    @Override
    public void run() {
        Gson gson = new Gson();
        DarCaja darObjetos = gson.fromJson(str, DarCaja.class);

        ArrayList<ObjetoMC> objetos = darObjetos.getObjetos();

        int chunkSize = 27;
        AtomicInteger counter = new AtomicInteger();
        final Collection<List<ObjetoMC>> cajas =
                objetos.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize))
                        .values();




        for (List<ObjetoMC> caja : cajas) {
            ItemStack cofre = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "chest")));
            ListNBT list = new ListNBT();

            for (int i = 0; i < caja.size(); i++) {
                CompoundNBT objetoNBT = new CompoundNBT();
                ObjetoMC objeto = caja.get(i);
                objetoNBT.putInt("Slot", i);
                objetoNBT.putString("id", objeto.getId());
                objetoNBT.putInt("Count", objeto.getCantidad());
                list.add(i, objetoNBT.copy());
            }

            CompoundNBT items = new CompoundNBT();
            items.put("Items", list.copy());


            CompoundNBT display = new CompoundNBT();
            display.putString("Name", "{\"text\":\"Paquete\", \"color\": \"aqua\"}");


            CompoundNBT nbt = cofre.getOrCreateTag();
            nbt.put("BlockEntityTag", items);
            nbt.put("display", display);

            ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), cofre);
            itemEntity.setPickUpDelay(0);

            World world = player.level;
            world.addFreshEntity(itemEntity);

        }


        /*
        HashMap<String, Integer> recompensas = new HashMap<>();
        for (int i = 0; i < objetos.size(); i++) {
            String nombre = objetos.get(i);
            if (!recompensas.containsKey("")) {
                recompensas.put(nombre, 1);
            } else {
                recompensas.put(nombre, recompensas.get(nombre) + 1);
            }
        }

        String mensaje = Lizardon.HEADER_MENSAJE + TextFormatting.WHITE + "Has obtenido: ";
        World world = player.level;
        for (Map.Entry me : recompensas.entrySet()) {
            String nombre = (String) me.getKey();
            int cantidad = (int) me.getValue();
            String[] partes = nombre.split(":");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(partes[0], partes[1]));
            ItemStack stack = new ItemStack(item, cantidad);

            ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), stack);
            itemEntity.setPickUpDelay(0);

            mensaje += TextFormatting.DARK_GREEN + stack.getDisplayName().getString() + TextFormatting.GOLD + " x" + cantidad + TextFormatting.WHITE + ", ";
            world.addFreshEntity(itemEntity);
        }
        StringTextComponent msg = new StringTextComponent(mensaje.substring(0, mensaje.length() - 2));
        player.sendMessage(msg, ChatType.SYSTEM, Util.NIL_UUID);*/
    }

    public static SMessageDarCaja decode(PacketBuffer buf) {
        SMessageDarCaja message = new SMessageDarCaja(buf.toString(Charsets.UTF_8));
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
