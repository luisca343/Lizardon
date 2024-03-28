package es.boffmedia.teras.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.ShopkeeperEvent;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.objects.ShopTransaction;
import es.boffmedia.teras.objects_old.dex.ActualizarDex;
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
import java.util.Objects;

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
        ItemStack itemStack;

        boolean encontrado = false;
        String currentLanguage = event.getEntityPlayer().getLanguage();
        ArrayList<ShopItemWithVariation> objetos;

        if(event instanceof ShopkeeperEvent.Purchase){
            itemStack = ((ShopkeeperEvent.Purchase) event).getItem();
            ((ShopkeeperEvent.Purchase) event).getItem();
            objetos = npc.getItemList();

        }else{
            itemStack = ((ShopkeeperEvent.Sell) event).getItem();
            objetos = npc.getSellList(event.getEntityPlayer());
        }

        for (ShopItemWithVariation objeto : objetos) {
            if(Objects.equals(objeto.getItemStack().getDisplayName().getString(), itemStack.getDisplayName().getString())){
                String npcName = ((NPCShopkeeper) event.getNpc()).getShopkeeperName(currentLanguage);
                String itemName = itemStack.getDisplayName().getString();
                String operation = event instanceof ShopkeeperEvent.Purchase ? "COMPRA" : "VENTA";

                int unitPrice = event instanceof ShopkeeperEvent.Purchase ? objeto.getBuyCost() : objeto.getSellCost();
                int count = itemStack.getCount();

                ShopTransaction shopTransaction = new ShopTransaction(event.getEntityPlayer().getStringUUID(), npcName, itemName, operation, unitPrice, count);
                Gson gson = new Gson();
                WingullAPI.wingullPOST("/starbank/shop", gson.toJson(shopTransaction));

                encontrado = true;
            }
        }

        if(!encontrado) {
            event.setCanceled(true);
        }
    }
}
