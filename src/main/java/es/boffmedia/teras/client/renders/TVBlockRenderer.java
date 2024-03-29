package es.boffmedia.teras.client.renders;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import es.boffmedia.teras.blocks.TVBlock;
import es.boffmedia.teras.tileentity.FrameBlockEntity;
import es.boffmedia.teras.util.math.CreateFrameBox;
import es.boffmedia.teras.util.displayers.IDisplay;
import es.boffmedia.teras.util.math.*;
import es.boffmedia.teras.util.math.*;
import me.srrapero720.watermedia.api.WaterMediaAPI;
import me.srrapero720.watermedia.api.image.ImageRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3i;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TVBlockRenderer extends TileEntityRenderer<FrameBlockEntity> {

    private static BufferedImage blackTextureBuffer = null;
    private static ImageRenderer blackTexture = null;

    private float tick;

    public TVBlockRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        if (blackTextureBuffer == null) {
            blackTextureBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            blackTextureBuffer.setRGB(0, 0, Color.TRANSLUCENT);
            blackTexture = new ImageRenderer(blackTextureBuffer);
        }
    }



    @Override
    public void render(FrameBlockEntity frame, float pPartialTick, MatrixStack pose, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
        /*
        if (frame.isURLEmpty()) {
            if (frame.display != null) frame.display.release();
            return;
        }*/


        IDisplay display = frame.requestDisplay();
        if (display == null) {
            if (!frame.isPlaying()) return;
            renderTexture(frame, null, WaterMediaAPI.api_getTexture(WaterMediaAPI.img_getLoading(), (int) tick, 1, true), pose, true);
            tick += pPartialTick / 2F;
            return;
        }

        if (frame.getUrl().isEmpty()) {
            frame.setUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/310px-Placeholder_view_vector.svg.png");
        }

        int texture = display.prepare(frame.getUrl(), frame.getVolume() * Minecraft.getInstance().options.getSoundSourceVolume(SoundCategory.MASTER), frame.minDistance, frame.maxDistance, frame.isPlaying(), frame.isLoop(), frame.getTick());

        if (texture == -1) {
            return;
        }

        renderTexture(frame, display, WaterMediaAPI.api_getTexture(blackTexture, 1, 1, false), pose, false);
        renderTexture(frame, display, texture, pose, true);
    }

    @Override
    public boolean shouldRenderOffScreen(FrameBlockEntity frame) {
        return frame.getSizeX() > 16 || frame.getSizeY() > 16;
    }
    private void renderTexture(FrameBlockEntity frame, IDisplay display, int texture, MatrixStack pose, boolean aspectRatio) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.clearColor(1, 1, 1, 1);


        RenderSystem.bindTexture(texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        Direction d = frame.getBlockState().getValue(TVBlock.FACING);
        if (d == Direction.NORTH) {
            d = Direction.SOUTH;
        } else if (d == Direction.SOUTH) {
            d = Direction.NORTH;
        }

        Facing facing = Facing.get(d);
        AlignedBox box = frame.getBox();



        float ancho = frame.getSizeX();
        float alto = frame.getSizeY();




        /*
        if (d == Direction.WEST) {
            box = new AlignedBox(0, 0, 0, 1, alto, ancho );
        }

        if (d == Direction.SOUTH) { // NORTE, ta bug
            box = new AlignedBox(0, 0, 0, ancho, alto, 1);
        }

        if (d == Direction.EAST) {
            box = new AlignedBox(0, 0, -ancho + 1, 1, alto, 1 );
        }

        if (d == Direction.NORTH) {
            box = new AlignedBox(-ancho +1, 0, 0, 1, alto, 0);
        }*/

        box = CreateFrameBox.getBox(box, d, (int) ancho, (int) alto, frame.getPosX(), frame.getPosY());


        BoxFace face = BoxFace.get(facing);
        pose.pushPose();



        if (d == Direction.NORTH) {
            pose.translate(0, 0, 0.9375);
        }

        if (d == Direction.SOUTH) {
            pose.translate(0, 0, -0.9375);
        }

        if (d == Direction.WEST) {
            pose.translate(0.9375, 0, 0);
        }

        if (d == Direction.EAST) {
            pose.translate(-0.9375, 0, 0);
        }

        //pose.translate(0.5, 0.5646, 0.5);
        pose.mulPose(facing.rotation().rotation((float) Math.toRadians(0)));

        //pose.translate(-0.5, -0.5, -0.5);

        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        Matrix4f mat = pose.last().pose();
        Matrix3f mat3f = pose.last().normal();
        Vector3i normal = face.facing.normal;
        for (BoxCorner corner : face.corners)
            builder.vertex(mat, box.get(corner.x), box.get(corner.y), box.get(corner.z))
                    .uv(corner.isFacing(face.getTexU()) ? 1 : 0, corner.isFacing(face.getTexV()) ? 1 : 0).color(-1 >> 16 & 255, -1 >> 8 & 255, -1 & 255, -1 >>> 24)
                    .normal(mat3f, normal.getX(), normal.getY(), normal.getZ()).endVertex();
        tesselator.end();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        pose.popPose();

        // Reset OpenGL state
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }
}