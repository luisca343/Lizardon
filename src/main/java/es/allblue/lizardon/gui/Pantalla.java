package es.allblue.lizardon.gui;

import es.allblue.lizardon.gui.botones.BotonComunista;
import es.allblue.lizardon.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;


@SideOnly(Side.CLIENT)
public class Pantalla extends GuiScreen {

    int backgroundWidth;
    int backgroundHeight;
    public int escala;
    private Pantalla pantallaAnteior;

    protected static int DEVICE_WIDTH;
    protected static int DEVICE_HEIGHT;

    protected int SCREEN_WIDTH;
    protected int SCREEN_HEIGHT;

    public static ResourceLocation CARCASA_MOVIL = new ResourceLocation("lizardon", "textures/movil/fondos/rotom.png");
    public static ResourceLocation FONDO_MOVIL = new ResourceLocation("lizardon", "textures/movil/fondos/rotom1.png");

    public int posX, posY;
    public int pantallaX, pantallaY;

    @Override
    public void initGui() {
        super.initGui();
        FONDO_MOVIL = new ResourceLocation("lizardon", "textures/movil/fondos/rotom1.png");
        renderMovil();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        this.drawDefaultBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        posX = (width - DEVICE_WIDTH) / 2;
        posY = (height - DEVICE_HEIGHT) / 2;

        this.mc.getTextureManager().bindTexture(CARCASA_MOVIL);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, DEVICE_WIDTH, DEVICE_HEIGHT, DEVICE_WIDTH, DEVICE_HEIGHT);

        dibujarFondo(FONDO_MOVIL);

        float size = .7f;
        String hora = StringUtils.ticksToElapsedTime((int) (Minecraft.getMinecraft().world.getWorldTime() % 24000));
        int textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(hora) * size);
        int panelHoraX = posX + SCREEN_WIDTH - textWidth;
        int panelHoraY = (int) (posY + DEVICE_HEIGHT * .04);
        GL11.glScalef(size, size, size);
        float mSize = (float) Math.pow(size, -1);
        fontRenderer.drawString(hora, panelHoraX / size, panelHoraY / size, Color.white.getRGB(), true);
        GL11.glScalef(mSize, mSize, mSize);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void renderMovil() {

        /* Creamos una resolución escalada para que el móvil se vea bien en todas las resoluciones*/
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        backgroundHeight = sr.getScaledHeight();
        backgroundWidth = sr.getScaledWidth();

        /* La altura del móvil será el 90% de la pantalla, y tendrá una relación de aspecto de 16:9*/
        DEVICE_HEIGHT = (int) (backgroundHeight * 0.8);
        DEVICE_WIDTH = DEVICE_HEIGHT * 9 / 16;

        /* Números raros para que la pantalla se dibuje dentro de los márgenes*/
        SCREEN_WIDTH = (int) (DEVICE_WIDTH - DEVICE_WIDTH / 12.5);
        SCREEN_HEIGHT = (int) (DEVICE_HEIGHT - (DEVICE_HEIGHT / 6));

        posX = (int) ((width - DEVICE_WIDTH) * 0.505);
        posY = (height - DEVICE_HEIGHT) / 2;

        pantallaX = getPantallaX();
        pantallaY = getPantallaY();

        //buttonList.add(new BotonComunista(pantallaX, pantallaY, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT,SCREEN_WIDTH, SCREEN_HEIGHT));
    }

    @Override
    protected void actionPerformed(GuiButton boton) throws IOException {
        if (boton.id == -1) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    public void dibujarFondo(ResourceLocation fondo) {
        this.mc.getTextureManager().bindTexture(fondo);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, DEVICE_WIDTH, DEVICE_HEIGHT, DEVICE_WIDTH, DEVICE_HEIGHT);

    }

    public int getPantallaX() {
        return (int) (posX + SCREEN_WIDTH / 30);
    } //21

    public int getPantallaY() {
        return (int) (SCREEN_HEIGHT / 3.75);
    }

    public int getPosX(double margenIzq, double ancho) {
        return (int) (pantallaX + SCREEN_WIDTH * .05 + margenIzq * SCREEN_WIDTH - ancho / 2);
    }

    public int getPosY(double margenArr, double alto) {
        return (int) (pantallaY + SCREEN_WIDTH * .05 + margenArr * SCREEN_HEIGHT - alto / 2);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(getPantallaAnteior());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void setPantallaAnteior(Pantalla pantalla) {
        this.pantallaAnteior = pantalla;
    }

    public Pantalla getPantallaAnteior() {
        return pantallaAnteior;
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        mc.displayGuiScreen((GuiScreen) null);
    }

    public void renderText(String texto, float escala, int posX, int posY, int color) {
        float size = escala;
        int textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(texto) * size);
        GL11.glScalef(size, size, size);
        float mSize = (float) Math.pow(size, -1);
        fontRenderer.drawString(texto, posX / escala, posY / escala, color, false);
        GL11.glScalef(mSize, mSize, mSize);
    }
}