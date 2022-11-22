package es.allblue.lizardon.net;

import es.allblue.lizardon.net.client.*;
import es.allblue.lizardon.net.server.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber
public class Messages {

    private static final String PROTOCOL_VERSION = "1";
    private static int index = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("lizardon", "packetsystem"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SubscribeEvent
    public static void registryNetworkPackets (FMLCommonSetupEvent event) {
        INSTANCE.registerMessage(index++, SMessagePadCtrl.class, SMessagePadCtrl::encode, SMessagePadCtrl::decode, SMessagePadCtrl::handle);
        INSTANCE.registerMessage(index++, SMessageIniciarLlamada.class, SMessageIniciarLlamada::encode, SMessageIniciarLlamada::decode, SMessageIniciarLlamada::handle);
        INSTANCE.registerMessage(index++, SMessageFinalizarLlamada.class, SMessageFinalizarLlamada::encode, SMessageFinalizarLlamada::decode, SMessageFinalizarLlamada::handle);
        INSTANCE.registerMessage(index++, SMessageDarObjetos.class, SMessageDarObjetos::encode, SMessageDarObjetos::decode, SMessageDarObjetos::handle);
        INSTANCE.registerMessage(index++, SMessageEncenderPC.class, SMessageEncenderPC::encode, SMessageEncenderPC::decode, SMessageEncenderPC::handle);
        INSTANCE.registerMessage(index++, SMessageVerMisiones.class, SMessageVerMisiones::encode, SMessageVerMisiones::decode, SMessageVerMisiones::handle);
        INSTANCE.registerMessage(index++, CMessageVerMisiones.class, CMessageVerMisiones::encode, CMessageVerMisiones::decode, CMessageVerMisiones::handle);
        INSTANCE.registerMessage(index++, SMessageDatosServer.class, SMessageDatosServer::encode, SMessageDatosServer::decode, SMessageDatosServer::handle);
        INSTANCE.registerMessage(index++, CMessageDatosServer.class, CMessageDatosServer::encode, CMessageDatosServer::decode, CMessageDatosServer::handle);
        INSTANCE.registerMessage(index++, CMessageCambioRegion.class, CMessageCambioRegion::encode, CMessageCambioRegion::decode, CMessageCambioRegion::handle);
    }
}
