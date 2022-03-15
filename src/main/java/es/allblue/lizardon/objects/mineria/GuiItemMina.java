package es.allblue.lizardon.objects.mineria;


import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.gui.mineria.CuadranteMineria;
import es.allblue.lizardon.gui.mineria.RecompensasMina;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiItemMina {
    // margenX + x * cuadranteX, margenY + (int) (backgroundHeight / 6.1 + y * cuadranteY), 0, 0, cuadranteX*ancho, cuadranteY*alto, cuadranteX*ancho, cuadranteY*alto
    int x;
    int y;
    int anchoX;
    int anchoY;

    int cuadranteX;
    int cuadranteY;
    RecompensaMina recompensaMina;
    boolean minado = false;

    public GuiItemMina(int x, int y, int anchoX, int anchoY, int cuadranteX, int cuadranteY, RecompensaMina recompensaMina) {
        this.x = x;
        this.y = y;
        this.anchoX = anchoX;
        this.anchoY = anchoY;
        this.cuadranteX = cuadranteX;
        this.cuadranteY = cuadranteY;
        this.recompensaMina = recompensaMina;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getAnchoX() {
        return anchoX;
    }

    public int getAnchoY() {
        return anchoY;
    }

    public int getCuadrantesAncho() {
        return this.recompensaMina.getAncho();
    }

    public int getCuadrantesAlto() {
        return this.recompensaMina.getAlto();
    }

    public ResourceLocation getResourceLocation() {
        return recompensaMina.getImagen();
    }

    public Item getItem() {
        return Item.REGISTRY.getObject(recompensaMina.getObjeto());
    }

    public boolean overlaps(int cuadranteX, int cuadranteY, int ancho, int alto) {
        int x0 = this.cuadranteX;
        int xf = this.cuadranteX + this.getCuadrantesAncho();

        int y0 = this.cuadranteY;
        int yf = this.cuadranteY + this.getCuadrantesAlto();

        if ((cuadranteX > xf || cuadranteX + ancho < x0) && (cuadranteY > yf || cuadranteY + alto < y0)) {
            return false;
        }

        return true;

        /*
        13,1 => 15, 3

        13,2 => 14, 3


        Point a1 = new Point(this.cuadranteX, this.cuadranteY);
        Point a2 = new Point(this.cuadranteX+getCuadrantesAnchoX(), this.cuadranteY+getCuadrantesAnchoY());

        Point b1 = new Point(cuadranteX, cuadranteY);
        Point b2 = new Point(cuadranteX+ancho, cuadranteY+alto);

        if(a1.x > b2.x || b1.x > a2.x || a1.y > b2.y || b1.y > a2.y){
            return false;
        }

        return true;*/
    }

    public boolean isMinado() {
        return minado;
    }

    public void setMinado() {
        this.minado = true;
    }

    public boolean comprobarMinado(CuadranteMineria[][] tablero) {
        if (!minado) {
            for (int x = cuadranteX; x < cuadranteX + getCuadrantesAncho(); x++) {
                for (int y = cuadranteY; y < cuadranteY + getCuadrantesAlto(); y++) {
                    if (tablero[x][y].getEstado() > 0) {
                        return false;
                    }
                }
            }
            setMinado();
        }
        return true;
    }
}
