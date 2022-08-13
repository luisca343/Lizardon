package es.allblue.lizardon.gui.botones;

import es.allblue.lizardon.gui.Pantalla;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Boton extends GuiButtonImage {
    private int u;
    private int v;
    private int textureWidth;
    private int textureHeight;
    private Pantalla pantalla;
    ResourceLocation fondo;

    public Boton(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, Pantalla pantalla, ResourceLocation fondo) {
        super(x, y, u, v, width, height, textureWidth, textureHeight, fondo);
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.textureHeight = textureHeight;
        this.textureWidth = textureWidth;
        this.pantalla = pantalla;
        this.fondo = fondo;
    }

    @Override
    public void drawButton(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_, float p_drawButton_4_) {
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(fondo);
            drawModalRectWithCustomSizedTexture(x, y , 0, 0, width, height, textureWidth, textureHeight);
        }
    }



    @Override
    public boolean mousePressed(Minecraft p_mousePressed_1_, int posX, int posY) {
        if(posX > x && posX < x+width && posY > y && posY < y+height){
            Minecraft.getMinecraft().displayGuiScreen(this.pantalla);
            return true;
        }
        return false;
    }
}

