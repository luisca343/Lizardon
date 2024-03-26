package es.boffmedia.teras.objects.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import es.boffmedia.teras.Teras;

public class PokemonData {
    private String naturaleza;
    private String especie;
    private String nombre;
    private int nivel;
    private String item;
    private String habilidad;
    private String[] ataques;
    private String[] ivs;
    private String[] evs;
    private String[] stats;

    public PokemonData(Pokemon pokemon) {

        especie = pokemon.getSpecies().getName();
        nombre = pokemon.getDisplayName();
        nivel = pokemon.getPokemonLevel();
        item = pokemon.getHeldItemAsItemHeld().getLocalizedName();
        habilidad = pokemon.getAbility().getLocalizedName();
        naturaleza = pokemon.getNature().getLocalizedName();
        ataques = new String[4];
        for (int i = 0; i < 4; i++) {
            if(pokemon.getMoveset().get(i) == null) continue;
            ataques[i] = pokemon.getMoveset().get(i).getMove().getLocalizedName();
        }
        ivs = new String[6];
        for (int i = 0; i < 6; i++) {
            if (pokemon.getIVs() == null) continue;
            ivs[i] = String.valueOf(pokemon.getIVs().getArray()[i]);
        }
        evs = new String[6];
        for (int i = 0; i < 6; i++) {
            if (pokemon.getEVs() == null) continue;
            evs[i] = String.valueOf(pokemon.getEVs().getArray()[i]);
        }
        stats = new String[6];
        stats[0] = String.valueOf(pokemon.getStats().getHP());
        stats[1] = String.valueOf(pokemon.getStats().getAttack());
        stats[2] = String.valueOf(pokemon.getStats().getDefense());
        stats[3] = String.valueOf(pokemon.getStats().getSpecialAttack());
        stats[4] = String.valueOf(pokemon.getStats().getSpecialDefense());
        stats[5] = String.valueOf(pokemon.getStats().getSpeed());
    }

    public String toString(){
        return Teras.GSON.toJson(this);
    }

    public String getEspecie() {
        return especie;
    }

    public String getNombre() {
        return nombre;
    }

    public String getItem() {
        return item;
    }

    public String getHabilidad() {
        return habilidad;
    }

    public String[] getAtaques() {
        return ataques;
    }

    public String[] getIvs() {
        return ivs;
    }

    public String[] getEvs() {
        return evs;
    }

    public String[] getStats() {
        return stats;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setHabilidad(String habilidad) {
        this.habilidad = habilidad;
    }

    public void setAtaques(String[] ataques) {
        this.ataques = ataques;
    }

    public void setIvs(String[] ivs) {
        this.ivs = ivs;
    }

    public void setEvs(String[] evs) {
        this.evs = evs;
    }

    public void setStats(String[] stats) {
        this.stats = stats;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }
}

