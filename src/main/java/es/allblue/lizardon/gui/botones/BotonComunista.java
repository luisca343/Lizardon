package es.allblue.lizardon.gui.botones;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BotonComunista extends GuiButtonImage {
    private int u;
    private int v;
    private int textureWidth;
    private int textureHeight;
    ResourceLocation fondo = new ResourceLocation("lizardon", "textures/marco.png");

    public BotonComunista(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
        super(x, y, u, v, width, height, textureWidth, textureHeight, new ResourceLocation("lizardon", "textures/marco.png"));
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.textureHeight = textureHeight;
        this.textureWidth = textureWidth;
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

            return true;
        }
        return false;
    }
}

