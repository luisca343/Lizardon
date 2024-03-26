package es.boffmedia.teras.objects.misiones;

import java.util.ArrayList;
import java.util.HashMap;

public class MisionesJugador {
    ArrayList<DatosMision> misiones;
    HashMap<String, Integer> categorias;

    public MisionesJugador(){
        this.misiones = new ArrayList<>();
        this.categorias = new HashMap<>();
    }

    public ArrayList<DatosMision> getMisiones() {
        return misiones;
    }

    public void setMisiones(ArrayList<DatosMision> misiones) {
        this.misiones = misiones;
    }

    public HashMap<String, Integer> getCategorias() {
        return categorias;
    }

    public void setCategorias(HashMap<String, Integer> categorias) {
        this.categorias = categorias;
    }
}
