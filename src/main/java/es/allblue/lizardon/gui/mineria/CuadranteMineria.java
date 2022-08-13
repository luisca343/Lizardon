package es.allblue.lizardon.gui.mineria;


import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.mineria.GuiItemMina;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import es.allblue.lizardon.sound.SoundRegistrator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class CuadranteMineria  extends GuiButtonImage {
    private int u;
    private int v;
    private int textureWidth;
    private int textureHeight;
    private int i;
    private int j;

    FontRenderer fontRenderer;
    private static  ResourceLocation texBase = new ResourceLocation(Lizardon.MODID, "textures/subsuelo/dirt.png");
    Mineria instance;
    int estado;

    public CuadranteMineria(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int i, int j, Mineria instance, int estado) {

        super(x, y, u, v, width, height, textureWidth, textureHeight, texBase);
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.textureHeight = textureHeight;
        this.textureWidth = textureWidth;
        this.i = i;
        this.j = j;
        this.instance = instance;
        this.estado = estado;
    }

    @Override
    public void drawButton(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_, float p_drawButton_4_) {
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(getTextura());
            drawModalRectWithCustomSizedTexture(x,y,u,v,width,height,textureWidth,textureHeight);
            GlStateManager.enableDepth();
        }
    }



    @Override
    public boolean mousePressed(Minecraft p_mousePressed_1_, int posX, int posY) {
        CuadranteMineria[][] tablero = instance.getTablero();
        if(posX > x && posX < x+width && posY > y && posY < y+height) {
            instance.updateDamage();
            getArea(i, j, tablero);
            comprobarRecompensas();
            dibujarGolpe();
        }
        return false;
    }

    private void dibujarGolpe() {
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Lizardon.MODID, "textures/subsuelo/martillo0.png"));
            drawModalRectWithCustomSizedTexture(x,y,u,v,100,100,100,100);
            GlStateManager.enableDepth();
    }

    private void comprobarRecompensas() {
        ArrayList<GuiItemMina> recompensas = instance.getRecompensas();
        for (GuiItemMina recompensa: recompensas){
            recompensa.comprobarMinado(instance.getTablero());
        }
    }

    private ResourceLocation getTextura(){
        switch(estado){
            /*
            case 6:
                return new ResourceLocation(Lizardon.MODID, "textures/subsuelo/muro6.png");
            case 5:
                return new ResourceLocation(Lizardon.MODID, "textures/subsuelo/muro5.png");
            case 4:
                return new ResourceLocation(Lizardon.MODID, "textures/subsuelo/muro4.png");
            case 3:
                return new ResourceLocation(Lizardon.MODID, "textures/subsuelo/muro3.png");
            case 2:
                return new ResourceLocation(Lizardon.MODID, "textures/subsuelo/muro2.png");
            case 1:
                return new ResourceLocation(Lizardon.MODID, "textures/subsuelo/muro1.png");*/
            default:
                return new ResourceLocation(Lizardon.MODID, "textures/trans.png");
        }
    }



    public void getArea(int row, int col, CuadranteMineria[][]tablero){
        int rowStart  = Math.max( row - 1, 0   );
        int rowFinish = Math.min( row + 1, tablero.length - 1 );
        int colStart  = Math.max( col - 1, 0   );
        int colFinish = Math.min( col + 1, tablero[0].length - 1 );

        for ( int curRow = rowStart; curRow <= rowFinish; curRow++ ) {
            for ( int curCol = colStart; curCol <= colFinish; curCol++ ) {
                CuadranteMineria cuadranteMineria = tablero[curRow][curCol];
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if(instance.getHerrramienta()==instance.MARTILLO){
                    golpearMartillo(player, curRow, row, curCol, col, cuadranteMineria);
                }else{
                    golpearPico(player, curRow, row, curCol, col, cuadranteMineria);
                }


            }
        }
    }


    private void golpearMartillo(EntityPlayerSP player, int curRow, int row, int curCol, int col, CuadranteMineria cuadranteMineria){
        player.playSound(SoundRegistrator.HAMMER,.05f,1f);
        //El cuadrante que has picado
        if(curRow==row&&curCol==col){
            cuadranteMineria.estado-=2;
        }
        //Cuadrantes en diagonal
        else if(curRow-row!=0 && curCol-col!=0){
            cuadranteMineria.estado--;
            //Los cuadrantes de los lados
        }else{
            cuadranteMineria.estado-=2;
        }

    }

    private void golpearPico(EntityPlayerSP player, int curRow, int row, int curCol, int col, CuadranteMineria cuadranteMineria){
        player.playSound(SoundRegistrator.PICK,.05f,1f);
        //El cuadrante que has picado
        if(curRow==row&&curCol==col){
            cuadranteMineria.estado-=2;
        }
        //Cuadrantes en diagonal
        else if(curRow-row!=0 && curCol-col!=0){
            //Los cuadrantes de los lados
        }else{
            cuadranteMineria.estado-=1;
        }
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}

