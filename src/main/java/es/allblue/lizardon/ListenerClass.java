package es.allblue.lizardon;

import es.allblue.lizardon.net.LizardonPacketHandler;
import es.allblue.lizardon.net.message.InputMessage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.montoyo.mcef.example.BrowserScreen;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "lizardon", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ListenerClass {

    public static final KeyBinding TEST = new KeyBinding("key.structure.desc", GLFW.GLFW_KEY_F11, "key.magicbeans.category");

    @SubscribeEvent(priority= EventPriority.HIGHEST, receiveCanceled=true)
    public void onKeyPress(InputEvent.KeyInputEvent event)
    {
            if(TEST.isDown()){
                System.out.println("Boing");
                LizardonPacketHandler.INSTANCE.sendToServer(new InputMessage(TEST.getKey().getValue()));
                StringTextComponent test = new StringTextComponent("TTEST");


                BrowserScreen screen = new BrowserScreen();

                //Minecraft.getInstance().setScreen(screen);
                //Minecraft.getInstance().setScreen(screen);
                /**/
            }
    }
}