package es.allblue.lizardon.objects;

import net.minecraft.potion.PotionEffect;

public class EfectoPocion {
    PotionEffect effect;
    float chance;

    public EfectoPocion(PotionEffect effect, float chance) {
        this.effect = effect;
        this.chance = chance;
    }

    public PotionEffect getEffect() {
        return effect;
    }

    public void setEffect(PotionEffect effect) {
        this.effect = effect;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }
}
