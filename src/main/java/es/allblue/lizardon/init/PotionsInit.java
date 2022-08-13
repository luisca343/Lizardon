package es.allblue.lizardon.init;

import es.allblue.lizardon.items.potion.PocionParalisis;
import es.allblue.lizardon.items.potion.PotionBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;

public class PotionsInit {
    public static HashMap<String, PotionBase> POTIONS = new HashMap<>();

    public static final Potion POCION_PARALISIS = new PocionParalisis().setRegistryName(new ResourceLocation("lizardon:paralisis"));


    public static void init() {
        GameRegistry.findRegistry(Potion.class).register(POCION_PARALISIS);
    }
}
