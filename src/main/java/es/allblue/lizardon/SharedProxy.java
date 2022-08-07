package es.allblue.lizardon;

import es.allblue.lizardon.client.ClientProxy;
import net.minecraft.nbt.CompoundNBT;
import net.montoyo.mcef.utilities.Log;

public class SharedProxy {
    public void updatePad(int id, CompoundNBT tag, boolean isSelected) {}

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
    }

    public void displaySetPadURLGui(String padURL) {
        Log.error("Called SharedProxy.displaySetPadURLGui() on server side...");
    }

    public void openMinePadGui(int padId) {
        Log.error("Called SharedProxy.openMinePadGui() on server side...");
    }

    public ClientProxy.PadData getPadByID(int id) {
        Log.error("Called SharedProxy.getPadByID() on server side...");
        return null;
    }
}
