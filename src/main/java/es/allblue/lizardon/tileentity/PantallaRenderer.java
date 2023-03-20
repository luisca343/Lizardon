package es.allblue.lizardon.tileentity;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientProxy;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
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

@OnlyIn(Dist.CLIENT)
public class PantallaRenderer extends TileEntityRenderer<PantallaTE> {
    public PantallaRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(PantallaTE te, float p_225616_2_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

        IBrowser browser = te.pantallaCine.browser;

        if (browser == null) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("Browser is null"), UUID.randomUUID());
            te.pantallaCine.browser = Lizardon.INSTANCE.getAPI().createBrowser("https://www.google.com", false);
            return;
        }

        if (browser.getTextureID() == 0) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("Texture is null"), UUID.randomUUID());
            return;
        }

        /*
        RenderSystem.disableCull();
        matrixStack.pushPose();
        matrixStack.translate(0.063f, 0.28f, 0.001f);
        GlStateManager._disableDepthTest();
        GlStateManager._enableTexture();
        browser.draw(matrixStack,0.0, 0.0, 27.65 / 32.0 + 0.01, 14.0 / 32.0 + 0.00);
        GlStateManager._enableDepthTest();
        matrixStack.popPose();
//        glDisable(GL_RESCALE_NORMAL);
        RenderSystem.enableCull();*/

        int x = 16;
        int y = 16;

        float sw = ((float) x) * 0.5f - 2.f / 16.f;
        float sh = ((float) y) * 0.5f - 2.f / 16.f;

        GL11.glPushMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, browser.getTextureID() );
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(1.f, 1.f, 1.f, 1.f);GL11.glTexCoord2f(0.0f, 1.0f);GL11.glVertex3f(-sw, -sh, 0.505f);
        GL11.glColor4f(1.f, 1.f, 1.f, 1.f);GL11.glTexCoord2f(1.0f, 1.0f);GL11.glVertex3f(-sw, -sh, 0.505f);
        GL11.glColor4f(1.f, 1.f, 1.f, 1.f);GL11.glTexCoord2f(1.0f, 0.0f);GL11.glVertex3f(-sw, -sh, 0.505f);
        GL11.glColor4f(1.f, 1.f, 1.f, 1.f);GL11.glTexCoord2f(0.0f, 0.0f);GL11.glVertex3f(-sw, -sh, 0.505f);
        GL11.glEnd();
        GlStateManager._bindTexture(0);
        GL11.glPopMatrix();

        // Use GL11 to render the browser texture

        /*
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, browser.getTextureID() );
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(1.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(0.0f, 1.0f);
        GL11.glEnd();
        GlStateManager._bindTexture(0);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();*/

        /*
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, browser.getTextureID() );
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(1.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(0.0f, 1.0f);
        GL11.glEnd();
        GlStateManager._bindTexture(0);
        GL11.glPopMatrix();*/


    }



    public static void renderCosa(){

    }
}
