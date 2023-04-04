package es.allblue.lizardon.objects;

import es.allblue.lizardon.Lizardon;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.UUID;

public class Entrenador {
    int dinero;
    ArrayList<Recompensa> recompensas;

    public Entrenador(int dinero, ArrayList<Recompensa> recompensas) {
        this.dinero = dinero;
        this.recompensas = recompensas;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public ArrayList<Recompensa> getRecompensas() {
        return recompensas;
    }

    public void setRecompensas(ArrayList<Recompensa> recompensas) {
        this.recompensas = recompensas;
    }

    public void recibirRecompensas(UUID uuid){
        Lizardon.PROXY.darObjetos(recompensas, uuid);
    }
}
