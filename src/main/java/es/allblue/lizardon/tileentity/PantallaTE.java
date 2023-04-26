package es.allblue.lizardon.tileentity;

import es.allblue.lizardon.client.gui.PantallaCine;
import es.allblue.lizardon.init.TileEntityInit;
import es.allblue.lizardon.util.BlockSide;
import es.allblue.lizardon.util.vector.Vector2i;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.montoyo.mcef.api.IBrowser;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class PantallaTE extends TileEntity {
    public IBrowser browser;
    public Direction facing;

    public int ancho;
    public int alto;

    public void setDimensions(int x, int y) {
        ancho = x;
        alto = y;
    }


    public class PantallaFake{

    }

    public PantallaTE() {
        super(TileEntityInit.TEST_PANTALLA.get());
    }

    public PantallaTE(Direction facing) {
        super(TileEntityInit.TEST_PANTALLA.get());
        this.facing = facing;
    }



    public PantallaCine getPantallaCine() {
        return new PantallaCine(browser);
    }


    

}
