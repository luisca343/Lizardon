package es.allblue.lizardon.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "lizardon")
public class TestRender
{
    @SubscribeEvent
    public static void worldRender(RenderWorldLastEvent event)
    {
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        IVertexBuilder builder = buffer.getBuffer(RenderType.LINES); //SpellRender.QUADS is a personal RenderType, of VertexFormat POSITION_COLOR.

        MatrixStack stack = event.getMatrixStack();

        stack.pushPose();

        Vector3d cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        stack.translate(-cam.x, -cam.y, -cam.z);

        Matrix4f mat = stack.last().pose();

        builder.vertex(mat, 0, 57, 0).color(0, 255, 255, 150).endVertex();
        builder.vertex(mat, 1, 57, 0).color(0, 255, 255, 150).endVertex();
        builder.vertex(mat, 1, 58, 0).color(0, 255, 255, 150).endVertex();
        builder.vertex(mat, 0, 58, 0).color(0, 255, 255, 150).endVertex();

        stack.popPose();

        buffer.endBatch(RenderType.LINES);
    }
}