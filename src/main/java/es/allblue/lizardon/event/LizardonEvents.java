package es.allblue.lizardon.event;


import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.items.SmartRotom;
import es.allblue.lizardon.objects.requests.ActualizarDinero;
import es.allblue.lizardon.objects.requests.PlayerData;
import es.allblue.lizardon.objects.requests.Transaccion;
import es.allblue.lizardon.util.RestApi;
import es.allblue.lizardon.util.Util;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.montoyo.wd.WebDisplays;

import java.awt.*;

@Mod.EventBusSubscriber
public class LizardonEvents {
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void cancelarHuevos(ProjectileImpactEvent event) {
        if (event.getEntity() instanceof EntityEgg) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void quitarDinero(LivingDeathEvent event) {
        Entity e = event.getEntity();
        if (e instanceof EntityPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) e;
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(e.getUniqueID());
            Transaccion trans = new Transaccion(player.getName(), "Fundación Biomédica Internacional", "Factura", "Servicio de rehabilitación de jugadores inconscientes.", "https://archivo.lizardon.es/imagenes/pixelmon/iconos/fbi.png", -1000, 0, false);
            String res = RestApi.post("transaccion", trans);
        }
    }

    private static String getURL(ItemStack is) {
        if (is.getTagCompound() == null || !is.getTagCompound().hasKey("PadURL"))
            return WebDisplays.INSTANCE.homePage;
        else
            return is.getTagCompound().getString("PadURL");
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void smartRotom(PlayerInteractEvent.RightClickItem event) {
        ItemStack item = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();

        Vec3d startVec = new Vec3d(player.posX, player.posY + player.eyeHeight, player.posZ);
        Vec3d lookVec = player.getLook(1.0F).scale(4);
        Vec3d endVec = startVec.add(lookVec);
        AxisAlignedBB boundingBox = player.getEntityBoundingBox().expand(lookVec.x, lookVec.y, lookVec.z).grow(1, 1, 1);
        RayTraceResult entityRayTraceResult = Util.rayTraceEntities(player, startVec, endVec, boundingBox, s -> s instanceof EntityLivingBase, 16);


        if (item.getItem() instanceof SmartRotom) {
            if (entityRayTraceResult != null) {
                EntityLivingBase entity = (EntityLivingBase) entityRayTraceResult.entityHit;
                if (entity instanceof EntityPixelmon) {
                    EntityPixelmon pixelmon = (EntityPixelmon) entity;
                    int idPokemon = pixelmon.getPokemonData().getSpecies().getNationalPokedexInteger();
                    int forma = pixelmon.getPokemonData().getForm();

                    if (item.getTagCompound() != null && item.getTagCompound().hasKey("PadID")) {
                        if (world.isRemote)
                            WebDisplays.PROXY.openMinePadGuiURL(item.getTagCompound().getInteger("PadID"), Lizardon.getProxy().pasarParametros("http://lizardon.es/smart_rotom/dex?pk=" + idPokemon + "&f=" + forma));
                    }
                }
            } else {
                if (player.isSneaking() && !item.hasTagCompound()) {
                    if (world.isRemote)
                        WebDisplays.PROXY.displaySetPadURLGui(getURL(item));

                } else if (item.getTagCompound() != null && item.getTagCompound().hasKey("PadID")) {
                    if (world.isRemote)
                        WebDisplays.PROXY.openMinePadGui(item.getTagCompound().getInteger("PadID"));

                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void coches(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();
        ItemStack item = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        if (entity.getName().equals("entity.mts_entity.name") && !player.world.isRemote) {
            if (item.getItem().getRegistryName().toString().equals("lizardon:coche")) {
                //Si las llaves son nuevas, asignarle la UUID
                if (!item.hasTagCompound()) {
                    NBTTagCompound newTag = new NBTTagCompound();
                    newTag.setString("ownerUUID", player.getUniqueID().toString());
                    item.setTagCompound(newTag);
                }

                NBTTagCompound nbt = entity.serializeNBT();
                String llaveUUID = item.getTagCompound().getString("ownerUUID");
                String cocheUUID = nbt.getString("ownerUUID");

                if(player.isSneaking()) return;
                //Recoger coche
                if (llaveUUID.equals(cocheUUID)) {
                    if (!item.getTagCompound().hasKey("uniqueUUID") || player.isCreative()) {
                        item.setTagCompound(nbt);
                        event.getWorld().removeEntity(entity);
                    } else {
                        player.sendMessage(new TextComponentString("Ya tienes un coche guardado!"));
                    }
                } else {
                    player.sendMessage(new TextComponentString("No puedes recoger un coche que no es tuyo."));
                }
            } else if(item.getItem().getRegistryName().toString().equals("lizardon:albaran")){
                NBTTagCompound tag = item.getTagCompound();

                String pos1 = tag.getString("pos1");
                String pos2 = tag.getString("pos2");

                BlockPos bPos1 = getPos(pos1);
                BlockPos bPos2 = getPos(pos2);

                boolean enArea = playerInPos(player.getPosition(), bPos1, bPos2);
                if(enArea){
                    event.getWorld().removeEntity(entity);
                    String itemId = item.getTagCompound().getString("item");
                    int cantidad = item.getTagCompound().getInteger("cantidad");

                    String[] partes = itemId.split(":");

                    Item recompensa = Item.REGISTRY.getObject(new ResourceLocation(partes[0], partes[1]));
                    ItemStack stack = new ItemStack(recompensa, cantidad);

                    player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, stack));

                    player.sendMessage(new TextComponentString("¡Misión cumplida! Obtienes "+cantidad+" "+stack.getDisplayName()));


                }else{
                    player.sendMessage(new TextComponentString("No estás en la zona de entrega."));
                }

            }else {
                //player.sendMessage(new TextComponentString("Estás en:" + entity.getPosition().toString()));
            }
        }
    }

    public static BlockPos getPos(String pos){
        String[] bPos = pos.split(",");
        return new BlockPos(Integer.parseInt(bPos[0]),Integer.parseInt(bPos[1]),Integer.parseInt(bPos[2]));
    }

    public static boolean playerInPos(BlockPos player, BlockPos pos1, BlockPos pos2){
        int x1, x2;
        if(pos1.getX() < pos2.getX()){
            x1 = pos1.getX();
            x2 = pos2.getX();
        }else{
            x1 = pos2.getX();
            x2 = pos1.getX();
        }

        int z1, z2;
        if(pos1.getZ() < pos2.getZ()){
            z1 = pos1.getZ();
            z2 = pos2.getZ();
        }else{
            z1 = pos2.getZ();
            z2 = pos1.getZ();
        }

        if(player.getX() >= x1 && player.getX() <= x2 && player.getZ() >= z1 && player.getZ() <= z2){
            return true;
        }
        return false;
    }


    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void rightClick(PlayerInteractEvent.RightClickBlock event) {
        ItemStack item = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        if (item.getItem().getRegistryName().toString().equals("lizardon:albaran") && !player.world.isRemote) {
            clickAlbaran("pos2", item, player, event.getPos());
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void leftClick(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack item = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        if (item.getItem().getRegistryName().toString().equals("lizardon:albaran") && !player.world.isRemote) {
            event.setCanceled(true);
            clickAlbaran("pos1", item, player, event.getPos());
        }
    }

    public static void clickAlbaran(String tipo, ItemStack item, EntityPlayer player, BlockPos pos){
        if(!player.isSneaking()) return;
        String posicion = pos.equals("pos1")? "primera" : "segunda";
        if(!item.hasTagCompound()){
            item.setTagCompound(new NBTTagCompound());
        }
        if(item.getTagCompound().hasKey("sellado")) return;
        item.getTagCompound().setString(tipo, posToString(pos));
        player.sendMessage(new TextComponentString("La "+posicion+" posición ha sido establecida a "+pos.toString()));
    }

    public static String posToString(BlockPos pos){
        return pos.getX()+","+pos.getY()+","+pos.getZ();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Lizardon.getProxy().onLogin(event);

    }


    @SubscribeEvent
    public static void connectToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Lizardon.getProxy().onConnect(event);
    }


    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public static void playerLoggedInEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            PlayerData data = new PlayerData(player.getUniqueID().toString(), player.getName());
            RestApi.post("register", data);
            RestApi.post("login", data);


            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueID());
            int balance = storage.getMoney();
            ActualizarDinero actualizarDinero = new ActualizarDinero(player.getName(), balance);
            RestApi.post("updateDinero", actualizarDinero);
        }
    }
}
