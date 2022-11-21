package es.allblue.lizardon.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageCambioRegion;
import es.allblue.lizardon.net.client.CMessageDatosServer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

@Mod.EventBusSubscriber(Dist.CLIENT)

public class RegionEvents {

    public static void onEnter(UUID uuid, String str){
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageCambioRegion(str));
    }

    public static  void onExit(UUID uuid, String str){
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageCambioRegion(str));
    }


}
