package es.allblue.lizardon.proxy;


import es.allblue.lizardon.Lizardon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy implements IProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {

    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        return ctx.getServerHandler().player;
    }

    @Override
    public void abrirMovil(){

    }

    @Override
    public void registerRender(Item item) {

    }

    @Override
    public void cambiarRenderer() {
    }


    @Override
    public void spawnItem(ItemStack itemStack) {

    }

    @Override
    public void abrirVentanaMineria() {

    }


    @Override
    public void abrirVentanaTest(String s) {

    }

    @Override
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Lizardon.getLogger().info(event.player.getName()+" se ha conectado");

    }

    @Override
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {

    }

    @Override
    public String pasarParametros(String urlBase) {
        return "null";
    }

}