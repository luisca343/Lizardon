package es.allblue.lizardon.event;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.commands.TestCommand;
import es.allblue.lizardon.init.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.event.QuestEvent;

import java.util.UUID;
import java.util.logging.Logger;

@Mod.EventBusSubscriber(modid = "lizardon")
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        new TestCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void test(NpcEvent.InteractEvent event){
        Lizardon.getLogger().info("Se ha interactuado con un NPC");

    }

    @SubscribeEvent
    public static void misionCumplida(QuestEvent.QuestCompletedEvent event){
        ServerPlayerEntity jugador = (ServerPlayerEntity) event.player.getMCEntity();
        jugador.sendMessage(new StringTextComponent("Has completado '" + event.quest.getName()+"'"), UUID.randomUUID());
        Lizardon.getLogger().info("FINALIZADA");
    }
}
