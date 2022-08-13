package es.allblue.lizardon;


import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import net.minecraft.client.Minecraft;

@ForgeVoicechatPlugin
public class ExampleVoicechatPlugin implements VoicechatPlugin {
    public static VoicechatServerApi SERVER_API;

    /**
     * @return the unique ID for this voice chat plugin
     */
    @Override
    public String getPluginId() {
        return Lizardon.MOD_ID;
    }

    /**
     * Called when the voice chat initializes the plugin.
     *
     * @param api the voice chat API
     */
    @Override
    public void initialize(VoicechatApi api) {
        //Lizardon.LOGGER.info("Example voice chat plugin initialized!");
        System.out.println("PATATTAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    /**
     * Called once by the voice chat to register all events.
     *
     * @param registration the event registration
     */
    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, evt -> SERVER_API = evt.getVoicechat());
    }

}