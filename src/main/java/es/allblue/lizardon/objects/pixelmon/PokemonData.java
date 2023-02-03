package es.allblue.lizardon.objects.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;

public class PokemonData {
    Species species;
    Stats form;

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Stats getForm() {
        return form;
    }

    public void setForm(Stats form) {
        this.form = form;
    }
}
