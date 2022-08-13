package es.allblue.lizardon.util;

import es.allblue.lizardon.Lizardon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LizardonSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
                DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Lizardon.MOD_ID);

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Lizardon.MOD_ID, name)));

    }

    public static final RegistryObject<SoundEvent> SMALL_EXLPOSION = registerSoundEvent("test");

    public static void register(IEventBus eventBus) {SOUND_EVENTS.register(eventBus);}

}
