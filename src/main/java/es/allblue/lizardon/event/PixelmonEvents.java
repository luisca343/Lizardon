package es.allblue.lizardon.event;

import com.pixelmonmod.pixelmon.api.events.ShopkeeperEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.blocks.machines.VendingMachineShop;
import com.pixelmonmod.pixelmon.blocks.tileentity.PokeStopTileEntity;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Mod.EventBusSubscriber
public class PixelmonEvents {

    @SubscribeEvent
    public void compraVenta(ShopkeeperEvent event){
        NPCShopkeeper npc = (NPCShopkeeper) event.getNpc();
        String operacion;
        ArrayList<ShopItemWithVariation> objetos = npc.getItemList();
        ItemStack itemStack;


        if(event instanceof ShopkeeperEvent.Purchase){
            itemStack = ((ShopkeeperEvent.Purchase) event).getItem();
            ((ShopkeeperEvent.Purchase) event).getItem();
            operacion = "COMPRA";
        }else{
            itemStack = ((ShopkeeperEvent.Sell) event).getItem();
            operacion = "VENTA";
        }

        boolean encontrado = false;
        for (ShopItemWithVariation objeto : objetos) {
            if(objeto.getItemStack().getItem().getRegistryName().toString().equals(itemStack.getItem().getRegistryName().toString())){
                int precioUnidad = event instanceof ShopkeeperEvent.Purchase ? objeto.getBuyCost() : objeto.getSellCost();
                int precioTotal = itemStack.getCount() * precioUnidad;
                encontrado = true;
                System.out.println("Se ha realizado una " + operacion + " de " + itemStack.getCount() + " " + itemStack.getDisplayName().getString() +" por " + precioTotal);
            }
        }
        if(!encontrado) {
            event.setCanceled(true);
            System.out.println("No se ha encontrado. Cancelando.");
        }
    }
}
