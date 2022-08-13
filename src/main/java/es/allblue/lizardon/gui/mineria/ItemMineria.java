package es.allblue.lizardon.gui.mineria;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemMineria extends GuiButtonImage {
    private int x,y,u,v,width,height,textureWidth,textureHeight,estado;
    private Mineria instance;
    private static  ResourceLocation texBase;
    private String nombre;

    public ItemMineria(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int i, int j, Mineria instance, ResourceLocation tex) {
        super(x, y, u, v, width, height, textureWidth, textureHeight, texBase);
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.instance = instance;
        this.nombre = nombre;
        this.estado = estado;
        this.texBase = tex;
    }


    @Override
    public void drawButton(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_, float p_drawButton_4_) {
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(texBase);
            drawModalRectWithCustomSizedTexture(x,y,u,v,width,height,textureWidth,textureHeight);
            GlStateManager.enableDepth();
        }
    }



}
