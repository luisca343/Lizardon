package es.allblue.lizardon.gui.movil;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.gui.Pantalla;
import es.allblue.lizardon.proxy.ClientProxy;
import es.allblue.lizardon.smartrotom.gui.WDScreen;
import es.allblue.lizardon.smartrotom.utilities.BlockSide;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;

public class PantallaPrincipal extends WDScreen {

    private ClientProxy.PadData pad;
    private double vx;
    private double vy;
    private double vw;
    private double vh;

    public IBrowser view;
    private boolean isInHotbar;
    private long lastURLSent;


    public PantallaPrincipal() {
    }

    public PantallaPrincipal(ClientProxy.PadData pad) {
        this.pad = pad;
    }

    @Override
    public void initGui() {
        super.initGui();

        vw = ((double) width) - 32.0f;
        vh = vw / Lizardon.PAD_RATIO;
        vx = 16.0f;
        vy = (((double) height) - vh) / 2.0f;
    }

    private static void addRect(BufferBuilder bb, double x, double y, double w, double h) {
        bb.pos(x, y, 0.0).endVertex();
        bb.pos(x + w, y, 0.0).endVertex();
        bb.pos(x + w, y + h, 0.0).endVertex();
        bb.pos(x, y + h, 0.0).endVertex();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ptt) {
        drawDefaultBackground();

        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glColor4f(0.73f, 0.73f, 0.73f, 1.0f);

        Tessellator t = Tessellator.getInstance();
        BufferBuilder bb = t.getBuffer();
        bb.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        addRect(bb, vx, vy - 16, vw, 16);
        addRect(bb, vx, vy + vh, vw, 16);
        addRect(bb, vx - 16, vy, 16, vh);
        addRect(bb, vx + vw, vy, 16, vh);
        t.draw();

        glEnable(GL_TEXTURE_2D);

        if(pad.view != null) {
            glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            pad.view.draw(vx, vy + vh, vx + vw, vy);
        }

        glEnable(GL_CULL_FACE);
    }

    @Override
    public void handleInput() {
        while(Keyboard.next()) {
            char key = Keyboard.getEventCharacter();
            int keycode = Keyboard.getEventKey();
            boolean pressed = Keyboard.getEventKeyState();

            if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                mc.displayGuiScreen(null);
                return;
            }

            if(pad.view != null) {
                if(pressed)
                    pad.view.injectKeyPressedByKeyCode(keycode, key, 0);
                else
                    pad.view.injectKeyReleasedByKeyCode(keycode, key, 0);

                if(key != 0)
                    pad.view.injectKeyTyped(key, 0);
            }
        }

        int vx = screen2DisplayX((int) this.vx);
        int vy = screen2DisplayY((int) this.vy);
        int vh = screen2DisplayX((int) this.vh);
        int vw = screen2DisplayY((int) this.vw);

        while(Mouse.next()) {
            int btn = Mouse.getEventButton();
            boolean pressed = Mouse.getEventButtonState();
            int sx = Mouse.getEventX();
            int sy = Mouse.getEventY();

            if(pad.view != null && sx >= vx && sx <= vx + vw && sy >= vy && sy <= vy + vh) {
                sx -= vx;
                sy -= vy;
                sy  = vh - sy;

                //Scale again according to the webview
                sx = (int) (((double) sx) / ((double) vw) * Lizardon.INSTANCE.padResX);
                sy = (int) (((double) sy) / ((double) vh) * Lizardon.INSTANCE.padResY);

                if(btn == -1)
                    pad.view.injectMouseMove(sx, sy, 0, false);
                else
                    pad.view.injectMouseButton(sx, sy, 0, btn + 1, pressed, 1);
            }
        }
    }

    @Override
    public void updateScreen() {
        if(pad.view == null)
            mc.displayGuiScreen(null); //In case the user dies with the pad in the hand
    }

    @Override
    public boolean isForBlock(BlockPos bp, BlockSide side) {
        return false;
    }
}
