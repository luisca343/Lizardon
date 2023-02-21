package es.allblue.lizardon;

import es.allblue.lizardon.client.ClientProxy;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.montoyo.mcef.utilities.Log;
import noppes.npcs.api.event.NpcEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class SharedProxy {
    public String idServidor;

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

    public void end (FMLLoadCompleteEvent event){

    }

    public int getNextPadID(){
        Log.error("Called SharedProxy.getNextPadID() on server side...");
        return 0;
    }

    public void actualizarNPC(NpcEvent.InteractEvent event) {
        Log.info("Esto no tira en server");
    }

    public void crearArchivo(String s) {
    }

    public Path getRuta(String carpeta) {
        System.out.println("Llamando getRuta en servidor");
        return Paths.get("");
    }

    public void setIdServidor(String idServidor){
        idServidor = "test";
    };

    public void closeSmartRotom(){
    }

    public void verVideo(String str) {
    }

    public void prepararNavegador() {
        System.out.println("Esto en servidor no hace nada");
    }
}
