package es.allblue.lizardon.gui.botones;

import es.allblue.lizardon.objects.Contacto;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;



import java.awt.*;

@SideOnly(Side.CLIENT)
public class BotonContacto extends GuiButtonImage {
    private int u;
    private int v;
    private int textureWidth;
    private int textureHeight;
    Contacto contacto;
    FontRenderer fontRenderer;

    public BotonContacto(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, Contacto contacto, FontRenderer fontRenderer) {
        super(x, y, u, v, width, height, textureWidth, textureHeight, contacto.getIcono());
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.textureHeight = textureHeight;
        this.textureWidth = textureWidth;
        this.contacto = contacto;
        this.fontRenderer = fontRenderer;
    }



    @Override
    public void drawButton(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_, float p_drawButton_4_) {
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(contacto.getIcono());
            drawModalRectWithCustomSizedTexture(x,y,u,v,width,height,textureHeight,textureWidth);
            GlStateManager.enableDepth();

            float size = .8f;
            String text = contacto.getNombre();
            int textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(text)*size);

            GL11.glScalef(size,size,size);
            float mSize = (float)Math.pow(size,-1);
            fontRenderer.drawString(text, (x+((textureWidth-textWidth)/2))/size, (int) (y+height)/size, Color.white.getRGB(),true);
            GL11.glScalef(mSize,mSize,mSize);
        }
    }



    @Override
    public boolean mousePressed(Minecraft p_mousePressed_1_, int posX, int posY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(contacto.getIcono());
        if(posX > x && posX < x+width && posY > y && posY < y+height){
            if(contacto.getUuid()=="Nuevo"){
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Creando contacto..."));
                return true;
            }else{
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Iniciando llamada"));

            }
            //Minecraft.getMinecraft().displayGuiScreen(contacto.getPantalla());
            return true;
        }
        return false;

    }
}

