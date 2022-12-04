package es.allblue.lizardon.event;

import com.pixelmonmod.pixelmon.api.events.ShopkeeperEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class PixelmonEvents {

    @SubscribeEvent
    public void compraVenta(ShopkeeperEvent event){
        System.out.println("EVENTO DE COMPRAVENTA");
        String nombreNPC = event.getNpc().getName().getString();
        NPCShopkeeper npc = (NPCShopkeeper) event.getNpc();
        String operacion;
        ArrayList<ShopItemWithVariation> objetos = npc.getItemList();
        ItemStack itemStack;

        if(event instanceof ShopkeeperEvent.Purchase){
            itemStack = ((ShopkeeperEvent.Purchase) event).getItem();
            operacion = "COMPRA";
        }else{
            itemStack = ((ShopkeeperEvent.Sell) event).getItem();
            operacion = "VENTA";
        }

        for (ShopItemWithVariation objeto : objetos) {
            if(objeto.getItemStack().getItem().getRegistryName().toString().equals(itemStack.getItem().getRegistryName().toString())){
                int precioUnidad = event instanceof ShopkeeperEvent.Purchase ? objeto.getBuyCost() : objeto.getSellCost();
                int precioTotal = itemStack.getCount() * precioUnidad;
                System.out.println("Se ha realizado una " + operacion + " de " + itemStack.getCount() + " " + itemStack.getDisplayName().getString() +" por " + precioTotal);
            }
        }
    }
}
