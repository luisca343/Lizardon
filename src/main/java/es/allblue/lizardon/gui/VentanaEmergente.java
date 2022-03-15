package es.allblue.lizardon.gui;

import net.minecraft.util.ResourceLocation;

public class VentanaEmergente extends Pantalla {

    public VentanaEmergente(Pantalla pantallaAnteior) {
        setPantallaAnteior(pantallaAnteior);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mc.getTextureManager().bindTexture(new ResourceLocation("lizardon", "textures/movil/ventana.png"));
        drawModalRectWithCustomSizedTexture(getPosX(.45, DEVICE_WIDTH * .8), getPosY(.3, DEVICE_WIDTH * .8 * 9 / 16), 0, 0, (int) (DEVICE_WIDTH * .8), (int) (DEVICE_WIDTH * .8 * 9 / 16), (int) (DEVICE_WIDTH * .8), (int) (DEVICE_WIDTH * .8 * 9 / 16));
    }
}