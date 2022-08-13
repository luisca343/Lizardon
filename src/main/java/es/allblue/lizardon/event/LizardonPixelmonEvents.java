package es.allblue.lizardon.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.EconomyEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.ShopkeeperEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.EventoDex;
import es.allblue.lizardon.objects.ItemTienda;
import es.allblue.lizardon.objects.requests.ActualizarDinero;
import es.allblue.lizardon.objects.requests.FinCombate;
import es.allblue.lizardon.objects.requests.Transaccion;
import es.allblue.lizardon.pixelmon.EnumFormasLizardon;
import es.allblue.lizardon.pixelmon.EnumMissingNo;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@Mod.EventBusSubscriber
public class LizardonPixelmonEvents {

    @SubscribeEvent
    public static void spawn(SpawnEvent event) {
        if (event.action instanceof SpawnActionPokemon) {
            EntityPixelmon pixelmon = (((SpawnActionPokemon) event.action).getOrCreateEntity());
            IEnumForm enumForm = pixelmon.getPokemonData().getFormEnum();
            if (enumForm instanceof EnumFormasLizardon) {
                String forma = enumForm.getName().substring(0, 1).toUpperCase() + enumForm.getName().substring(1);
                (((SpawnActionPokemon) event.action).getOrCreateEntity()).getPokemonData().setNickname(pixelmon.getName() + " " + forma);
            } else if (enumForm instanceof EnumMissingNo) {
                String forma = enumForm.getName().substring(0, 1).toUpperCase() + enumForm.getName().substring(1);
                (((SpawnActionPokemon) event.action).getOrCreateEntity()).getPokemonData().setNickname(forma);
            }
        }
        return;
    }

    @SubscribeEvent
    public static void pokedex(PokedexEvent event) {
        String mundo = "local";
        if (FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
            mundo = FMLCommonHandler.instance().getMinecraftServerInstance().getMOTD();
        } else {
            mundo = FMLCommonHandler.instance().getMinecraftServerInstance().getServerHostname();
        }

        EventoDex dex = new EventoDex(mundo, event.uuid.toString(), event.pokemon.getSpecies().getNationalPokedexInteger(), event.newStatus.name());
        Gson gson = new Gson();
        String json = gson.toJson(dex);
    }


    public void a(BattleEndEvent event) {

    }

    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public static void combate(BattleEndEvent event) {
        if (event.getPlayers().size() == 0) return;
        if (event.getPlayers().size() == 2) {
            EntityPlayerMP player2 = event.getPlayers().get(1);
            if (event.isCanceled()) {
                return;
            }
            if (!event.abnormal || event.results.entrySet().iterator().next().getValue() == BattleResults.DRAW) {
                return;
            }
            EntityPlayerMP ganador, perdedor;
            if (event.bc.getPlayers().get(0).isDefeated) {
                perdedor = event.getPlayers().get(0);
                ganador = event.getPlayers().get(1);
            } else {
                perdedor = event.getPlayers().get(1);
                ganador = event.getPlayers().get(0);
            }

            FinCombate combate = new FinCombate(ganador.getName(), perdedor.getName());
            RestApi.post("actualizarElo", combate);

        } else {
            if (event.getPlayers().get(0) != null) {
                EntityPlayerMP player = event.getPlayers().get(0);
                PixelmonWrapper pokemon = event.bc.getOppositePokemon(event.bc.getActivePokemon().get(0));
                if (pokemon.getBaseStats().getType1() == EnumType.Ghost || pokemon.getBaseStats().getType2() == EnumType.Ghost) {
                    for (ItemStack s : player.inventory.mainInventory) {
                        if (s != null && s.hasTagCompound() && s.getTagCompound().hasKey("almas")) {
                            int almas = s.getTagCompound().getInteger("almas");
                            if (almas++ <= 108)
                                s.getTagCompound().setInteger("almas", almas);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void gestionarCompraVenta(ShopkeeperEvent event) {
        if (Lizardon.itemsTienda.equals(null)) {
            Lizardon.cargarObjetosTiendas();
        }
        try {
            EntityPlayerMP player = event.getEntityPlayer();
            EntityNPC npc = event.getNpc();
            ItemStack item = null;
            String evento = "";
            String concepto = "";
            int precio = 0;

            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueID());
            int balance = storage.getMoney();

            if (event instanceof ShopkeeperEvent.Purchase) {
                evento += "Compra de ";
                concepto += "Compra ";
                ShopkeeperEvent.Purchase purchase = (ShopkeeperEvent.Purchase) event;
                item = purchase.getItem();
                String nombre = item.getItem().getRegistryName().toString();
                ItemTienda itemTienda = Lizardon.itemsTienda.get(nombre);
                precio = itemTienda.getCompra() * -item.getCount();
            } else {
                evento += "Venta de ";
                concepto += "Venta ";
                ShopkeeperEvent.Sell purchase = (ShopkeeperEvent.Sell) event;
                item = purchase.getItem();
                String nombre = item.getItem().getRegistryName().toString();
                ItemTienda itemTienda = Lizardon.itemsTienda.get(nombre);
                precio = itemTienda.getVenta() * item.getCount();
            }
            concepto += "a NPC";
            evento += item.getCount() + " " + item.getDisplayName();
            Transaccion trans = new Transaccion(player.getName(), "NPC", concepto, evento, "https://archivo.lizardon.es/imagenes/pixelmon/iconos/wingull.png", precio, balance + precio, true);
            String res = RestApi.post("transaccion", trans);
        } catch (Exception e) {
            Lizardon.getLogger().info(e.getMessage());
            Lizardon.getLogger().info("Ha petado el archivo de compras, qué bien :D");
        }
    }

    @SubscribeEvent
    public static void actualizarDinero(EconomyEvent.PostTransactionEvent event) {
        ActualizarDinero actualizarDinero = new ActualizarDinero(event.player.getName(), event.newBalance);
        RestApi.post("updateDinero", actualizarDinero);
    }


    @SubscribeEvent
    public static void comprar(ShopkeeperEvent.Purchase event) {
        gestionarCompraVenta(event);
    }


    @SubscribeEvent
    public static void vender(ShopkeeperEvent.Sell event) {
        gestionarCompraVenta(event);
    }


}
