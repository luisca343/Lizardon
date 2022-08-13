package es.allblue.lizardon.event;

import es.allblue.lizardon.Lizardon;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod.EventBusSubscriber
public class LizardonPackets {
    @SubscribeEvent
    public static void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Lizardon.getLogger().info("CANALCLIENTE: " + event.getPacket().channel());
        Lizardon.getLogger().info("CANALCLIENTE: " + event.getPacket().channel());
        String data = ByteBufUtils.readUTF8String(event.getPacket().payload());
        Minecraft.getMinecraft().player.sendChatMessage(data);
    }

    @SubscribeEvent
    public static void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        Lizardon.getLogger().info("CANALSERVIDOR: " + event.getPacket().channel());
        Lizardon.getLogger().info("CANALSERVIDOR: " + event.getPacket().channel());
        String data = ByteBufUtils.readUTF8String(event.getPacket().payload());
        Minecraft.getMinecraft().player.sendChatMessage(data);
    }
}
