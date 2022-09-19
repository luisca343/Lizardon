package es.allblue.lizardon.event;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.commands.TestCommand;
import es.allblue.lizardon.init.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.logging.Logger;

@Mod.EventBusSubscriber(modid = "lizardon")
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        new TestCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.EntityInteract event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        World world = event.getWorld();
        if(event.getItemStack().getItem().equals(ItemInit.SMARTROTOM.get())){
            if(!stack.hasTag()){
                stack.setTag(new CompoundNBT());
                stack.getTag().putString("PadURL", "http://www.google.es");
                int id = Lizardon.PROXY.getNextPadID();
                stack.getTag().putInt("PadID", id);
                Lizardon.getLogger().info("Se ha creado el Pad con ID "+id);
                Lizardon.PROXY.updatePad(id, stack.getTag(), true);
            }
            if(event.getTarget() instanceof  PixelmonEntity){
                Lizardon.getLogger().info("PIXELMON");
            }
        }
    }
}
