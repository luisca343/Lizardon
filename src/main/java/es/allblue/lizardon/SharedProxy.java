package es.allblue.lizardon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.client.gui.PantallaCine;
import es.allblue.lizardon.objects.Recompensa;
import es.allblue.lizardon.tileentity.PantallaTE;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.utilities.Log;
import noppes.npcs.api.event.NpcEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SharedProxy {
    public String idServidor;

    public void updatePad(int id, CompoundNBT tag, boolean isSelected) {}

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
    }

    public void displaySetPadURLGui(String padURL) {
        Log.error("Called SharedProxy.displaySetPadURLGui() on server side...");
    }

    public void openMinePadGui(int padId) {
        Log.error("Called SharedProxy.openMinePadGui() on server side...");
    }

    public ClientProxy.PadData getPadByID(int id) {
        Log.error("Called SharedProxy.getPadByID() on server side...");
        return null;
    }

    public void end (FMLLoadCompleteEvent event){

    }

    public int getNextPadID(){
        Log.error("Called SharedProxy.getNextPadID() on server side...");
        return 0;
    }

    public void actualizarNPC(NpcEvent.InteractEvent event) {
        Log.info("Esto no tira en server");
    }

    public void crearArchivo(String s) {
    }

    public Path getRuta(String carpeta) {
        System.out.println("Llamando getRuta en servidor");
        return Paths.get("");
    }

    public void setIdServidor(String idServidor) {
        this.idServidor = idServidor;
    }

    public void closeSmartRotom(){
    }

    public void verVideo(String str) {
    }

    public void prepararNavegador(API api) {
        System.out.println("Esto en servidor no hace nada");
    }

    public void abrirPantalla(Screen pantallaCine) {
        System.out.println("Esto en servidor no hace nada");
    }

    public void darObjetos(ArrayList<Recompensa> objetos, UUID uuid){
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);

        String mensaje = Lizardon.HEADER_MENSAJE + TextFormatting.WHITE + "Has obtenido: ";
        World world = player.level;
        for (Recompensa recompensa : objetos) {
            String nombre = recompensa.getObjeto();
            String[] partes = nombre.split(":");
            int cantidad = recompensa.getCantidad();

            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(partes[0], partes[1]));
            ItemStack stack = new ItemStack(item, cantidad);

            if(recompensa.getNbt() != null){
                String nbtStr = recompensa.getNbt();
                try {
                    CompoundNBT nbt = (CompoundNBT) JsonToNBT.parseTag(nbtStr);
                    Set<String> keys = nbt.getAllKeys();

                    CompoundNBT stackNBT = stack.getOrCreateTag();
                    for (String key : keys) {
                        stackNBT.put(key, nbt.get(key));
                    }

                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }



            ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), stack);
            itemEntity.setPickUpDelay(0);

            mensaje += TextFormatting.DARK_GREEN + stack.getDisplayName().getString() + TextFormatting.GOLD + " x" + cantidad + TextFormatting.WHITE + ", ";
            world.addFreshEntity(itemEntity);
        }

        StringTextComponent msg = new StringTextComponent(mensaje.substring(0, mensaje.length() - 2));
        player.sendMessage(msg, ChatType.SYSTEM, Util.NIL_UUID);
    }

    public void abrirPantallaCine(PantallaTE te) {
        System.out.println("Esto en servidor no hace nada");
    }
}
