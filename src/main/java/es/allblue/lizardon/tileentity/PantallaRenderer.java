package es.allblue.lizardon.tileentity;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.gui.PantallaCine;
import es.allblue.lizardon.util.BlockSide;
import es.allblue.lizardon.util.vector.Vector3f;
import es.allblue.lizardon.util.vector.Vector3i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.montoyo.mcef.api.IBrowser;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.util.math.vector.Vector3f.*;

@OnlyIn(Dist.CLIENT)
public class PantallaRenderer extends TileEntityRenderer<PantallaTE> {

    private final Vector3f mid = new Vector3f();
    private final Vector3i tmpi = new Vector3i(0,0,0);
    private final Vector3f tmpf = new Vector3f();

    public PantallaRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(PantallaTE te, float p_225616_2_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

        IBrowser browser = te.browser;

        if (browser == null) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("Browser is null"), UUID.randomUUID());
            te.browser = Lizardon.INSTANCE.getAPI().createBrowser("https://www.google.com", false);
            return;
        }

        if (browser.getTextureID() == 0) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("Texture is null"), UUID.randomUUID());
            return;
        }



        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        //TODO: don't use tesselator
        RenderSystem.enableDepthTest();
        //with forge 1.16.5 bind texture to RenderSystem

        int sizeX = 1;
        int sizeY = 1;
        float sw = ((float) sizeX) * 0.6f - 2.f / 16.f;
        float sh = ((float) sizeY) * 0.6f - 2.f / 16.f;
        matrixStack.pushPose();

        Tessellator t = Tessellator.getInstance();
        BufferBuilder vb = t.getBuilder();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        //enviar mensaje al jugador con la orientación de la pantalla
        Minecraft.getInstance().player.sendMessage(new StringTextComponent("Facing: " + te.facing), UUID.randomUUID());
        /**/
        BlockSide lado;
        switch (te.facing){
            case SOUTH:
                lado = BlockSide.SOUTH;
                break;
            case EAST:
                lado = BlockSide.EAST;
                break;
            case WEST:
                lado = BlockSide.WEST;
                break;
            case DOWN:
                lado = BlockSide.BOTTOM;
                break;
            case UP:
                lado = BlockSide.TOP;
                break;
            default:
                lado = BlockSide.NORTH;
                break;
        }


        tmpi.set(lado.right);
        tmpi.mul(sizeX);
        tmpi.addMul(lado.up, sizeY);
        tmpf.set(tmpi);
        mid.set(0.5, 0.5, 0.5);
        mid.addMul(tmpf, 0.5f);
        tmpf.set(lado.left);
        mid.addMul(tmpf, 0.5f);
        tmpf.set(lado.down);
        mid.addMul(tmpf, 0.5f);

        matrixStack.translate(mid.x, mid.y, mid.z);


        switch (te.facing){
            case DOWN:
                matrixStack.mulPose(XP.rotationDegrees(90.f + 49.8f));
                break;
            case UP:
                matrixStack.mulPose(XN.rotation(90.f + 49.8f));
                break;
            case NORTH:
                matrixStack.mulPose(YN.rotationDegrees(180.f));
                break;
            case SOUTH:
                break;
            case WEST:
                matrixStack.mulPose(YN.rotationDegrees(90.f));
                break;
            default:
                matrixStack.mulPose(YP.rotationDegrees(90.f));
                break;
        }



        RenderSystem.enableDepthTest();
        GlStateManager._bindTexture(browser.getTextureID() );
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);

        vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        builder.vertex(matrixStack.last().pose(), -sw, -sh, 0.505f).uv(0.f, 1.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
        builder.vertex(matrixStack.last().pose(), sw, -sh, 0.505f).uv(1.f, 1.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
        builder.vertex(matrixStack.last().pose(), sw, sh, 0.505f).uv(1.f, 0.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
        builder.vertex(matrixStack.last().pose(), -sw, sh, 0.505f).uv(0.f, 0.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
        tesselator.end();//Minecraft does shit with mah texture otherwise...
        RenderSystem.disableDepthTest();

        matrixStack.popPose();

    }



    public static void renderCosa(){

    }
}
