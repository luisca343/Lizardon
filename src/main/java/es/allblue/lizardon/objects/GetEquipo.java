package es.allblue.lizardon.objects;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public class GetEquipo {
    String query;
    List<PkmSlot> equipo;
    String tipo;


    public class PkmSlot{
        int caja;
        int slot;

        public int getCaja() {
            return caja;
        }

        public void setCaja(int caja) {
            this.caja = caja;
        }

        public int getSlot() {
            return slot;
        }

        public void setSlot(int slot) {
            this.slot = slot;
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<PkmSlot> getEquipo() {
        return equipo;
    }

    public void setEquipo(List<PkmSlot> equipo) {
        this.equipo = equipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
