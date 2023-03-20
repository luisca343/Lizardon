package es.allblue.lizardon.tileentity;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.gui.PantallaCine;
import es.allblue.lizardon.init.TileEntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.montoyo.mcef.api.IBrowser;

public class PantallaTE extends TileEntity {
    PantallaCine pantallaCine;

    public PantallaTE() {
        super(TileEntityInit.TEST_PANTALLA.get());
        pantallaCine = new PantallaCine("http://google.com");
    }


    public PantallaCine getPantallaCine() {
        return pantallaCine;
    }

    public void setPantallaCine(PantallaCine pantallaCine) {
        this.pantallaCine = pantallaCine;
    }
}
