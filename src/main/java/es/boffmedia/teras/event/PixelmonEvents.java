package es.boffmedia.teras.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.ShopkeeperEvent;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.objects.dex.ActualizarDex;
import es.boffmedia.teras.util.MessageHelper;
import es.boffmedia.teras.util.PersistentDataFields;
import es.boffmedia.teras.util.WingullAPI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class PixelmonEvents {


    @SubscribeEvent
    public void cancelarXP(ExperienceGainEvent event){
        try {
            if (!event.pokemon.hasOwner()) return;
        }catch (NullPointerException e){
            Teras.LOGGER.warn("Error al cancelar XP. Ignorando...");
            return;
        }
        ServerPlayerEntity player = event.pokemon.getPlayerOwner();
        if(!event.pokemon.getPlayerOwner().getPersistentData().getBoolean(PersistentDataFields.FB_ACTIVO.label)) return;
        MessageHelper.enviarMensaje(player, "No puedes ganar experiencia en el Frente Batalla");
        event.setCanceled(true);
    }



    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void guardarDex(PokedexEvent.Post event){
        String uuid = event.getPlayer().getStringUUID();
        int idPokemon = event.getPokemon().getSpecies().getDex();
        int estado = event.getNewStatus().equals(PokedexRegistrationStatus.SEEN) ? 0 : 1;
        if(estado == 1 && event.getPokemon().isShiny()) estado = 2;



        ActualizarDex dex = new ActualizarDex(uuid, idPokemon, estado);
        Gson gson = new Gson();

        WingullAPI.wingullPOST("/dex/registro", gson.toJson(dex));
    }
    
    @SubscribeEvent
    public void pokedex(PokedexEvent event){
        event.getOldStatus();
    }

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
            }
        }
        if(!encontrado) {
            event.setCanceled(true);
        }
    }
}
