package es.allblue.lizardon.event.wungill;

import com.mojang.blaze3d.systems.RenderSystem;
import es.allblue.lizardon.Lizardon;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RegionEventsClient {
    static String cartelActivo = "tulipan";
    static long tiempoInicio = 0;
    static int tiempoCartel = 0;
    static int tiempoTransicion = 500;
    private static ResourceLocation cartelActual;

    public static void renderizarCartel(String cartel, int tiempo){
        ResourceLocation imagen = new ResourceLocation(Lizardon.MOD_ID, "textures/carteles/" + cartel + ".png");
        try {
            Minecraft.getInstance().getResourceManager().getResource(imagen);
        } catch (IOException e) {
            imagen = new ResourceLocation(Lizardon.MOD_ID, "textures/carteles/null.png");
        }
        cartelActual = imagen;
        cartelActivo = cartel;
        tiempoCartel = tiempo * 1000 + tiempoTransicion * 2;

        Date currentDate = new Date();
        tiempoInicio = currentDate.getTime();
    }

    public static void renderizarCartel(String cartel, int tiempo, int trans){
        tiempoTransicion = trans;
        renderizarCartel(cartel, tiempo);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void renderGameOverlay(RenderGameOverlayEvent.Post event)
    {
        if(event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        long tiempoActual = new Date().getTime();
        if ((tiempoActual - tiempoInicio) >= tiempoCartel) return;
        long posY = 0;
        long tiempoPasado = tiempoActual - tiempoInicio;
        if (tiempoPasado < tiempoTransicion) {
            posY = tiempoPasado / 5 - 100;
        }
        if (tiempoPasado > (tiempoCartel - tiempoTransicion) ) {
            posY = (tiempoCartel - tiempoPasado) / 5 - 100;
        }
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        Minecraft.getInstance().getTextureManager().bind(cartelActual);
        Minecraft.getInstance().gui.blit(event.getMatrixStack(), 0, (int) posY, 0, 0, 256, 256);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
