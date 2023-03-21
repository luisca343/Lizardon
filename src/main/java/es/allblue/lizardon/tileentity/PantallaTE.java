package es.allblue.lizardon.tileentity;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.gui.PantallaCine;
import es.allblue.lizardon.init.TileEntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.montoyo.mcef.api.IBrowser;

public class PantallaTE extends TileEntity {
    public IBrowser browser;
    public Direction facing;

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
