package es.boffmedia.teras.objects.post;

public class PokedexSpawnChance {
    String pokemonName;
    double spawnChance;
    int dex;

    public PokedexSpawnChance(String pokemonName, double spawnChance, int dex) {
        this.pokemonName = pokemonName;
        this.spawnChance = spawnChance;
        this.dex = dex;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public double getSpawnChance() {
        return spawnChance;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public void setSpawnChance(double spawnChance) {
        this.spawnChance = spawnChance;
    }
}
