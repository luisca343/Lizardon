package es.allblue.lizardon.sound;

import es.allblue.lizardon.Lizardon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.ArrayList;

public class SoundRegistrator {
    ArrayList<SoundEvent> SoundEvents;
    public static final SoundEvent PICK;
    public static final SoundEvent HAMMER;
    public static final SoundEvent MUSICA_SUBSUELO;

    public static final SoundEvent TASER;

    static {
        PICK = addSoundsToRegistry("pick");
        HAMMER = addSoundsToRegistry("hammer");
        MUSICA_SUBSUELO = addSoundsToRegistry("underground");
        TASER = addSoundsToRegistry("taser");
    }

    private static SoundEvent addSoundsToRegistry(String soundId) {
        ResourceLocation shotSoundLocation = new ResourceLocation(Lizardon.MODID, soundId);
        SoundEvent soundEvent = new SoundEvent(shotSoundLocation);
        soundEvent.setRegistryName(shotSoundLocation);
        return soundEvent;
    }
}