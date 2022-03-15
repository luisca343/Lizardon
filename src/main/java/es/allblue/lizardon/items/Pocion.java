package es.allblue.lizardon.items;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.items.IMedicine;

public class Pocion implements IMedicine {


    @Override
    public boolean useMedicine(PokemonLink pokemonLink, double v) {
        return false;
    }

    @Override
    public boolean useMedicine(PokemonLink target) {
        return IMedicine.super.useMedicine(target);
    }
}
