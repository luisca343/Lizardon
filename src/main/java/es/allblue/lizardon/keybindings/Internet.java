package es.allblue.lizardon.keybindings;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

public class Internet extends KeyBinding {
    public Internet(){
        super("key.internet", GLFW.GLFW_KEY_F10, "key.categories.lizardon");
    }

    public void keyDown(InputEvent.KeyInputEvent event){
        System.out.println("ESTO HA SIDO PULSADDO");

    }
}
