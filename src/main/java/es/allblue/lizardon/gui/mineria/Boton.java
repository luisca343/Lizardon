package es.allblue.lizardon.gui.mineria;


import es.allblue.lizardon.Lizardon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Boton extends GuiButtonImage {
    private int x,y,u,v,width,height,textureWidth,textureHeight,estado;
    private Mineria instance;
    private static ResourceLocation texBase = new ResourceLocation(Lizardon.MODID, "textures/subsuelo/dirt.png");
    private String nombre;

    public Boton(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, Mineria instance, String nombre,int estado) {
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
    }

    public void cambiarEstado(){
        if(estado==0){
            estado=1;
        }else{
            estado=0;
        }
    }

    @Override
    public void drawButton(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_, float p_drawButton_4_) {
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(instance.getBotones().get(nombre+estado));
            drawModalRectWithCustomSizedTexture(x,y,u,v,width,height,textureWidth,textureHeight);
            GlStateManager.enableDepth();
        }
    }



    @Override
    public boolean mousePressed(Minecraft p_mousePressed_1_, int posX, int posY) {
        if(posX > x && posX < x+width && posY > y && posY < y+height) {
            if(estado==0){
                instance.cambiarHerramienta();
            }

        }

        return false;
    }



}
