package es.allblue.lizardon.event;

import es.allblue.lizardon.commands.TochiKartsCommand;
import es.allblue.lizardon.commands.CombateCommand;
import es.allblue.lizardon.commands.Discos;
import es.allblue.lizardon.commands.TestCommand;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;


@Mod.EventBusSubscriber
public class LizardonEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        new TestCommand(event.getDispatcher());
        new Discos(event.getDispatcher());
        new CombateCommand(event.getDispatcher());

        if(event.getEnvironment().compareTo(Commands.EnvironmentType.DEDICATED) == 0){
            new TochiKartsCommand(event.getDispatcher());
        }

        ConfigCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent ev){
        System.out.println("EN EL LOGIN Y TAL");
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) ev.getPlayer();

        boolean inicio = serverPlayer.getPersistentData().getBoolean("inicio");
        System.out.println("LA VARIABLE INICIO ES");
        System.out.println(inicio);
        /*
        if(!inicio){
            Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) ev.getPlayer()), new CMessageVerVideo("http://localhost:3000/video"));
            serverPlayer.getPersistentData().putBoolean("inicio", true);
        }*/

    }

}
