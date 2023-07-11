package es.allblue.lizardon.event;

import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.commands.TestCommand;
import es.allblue.lizardon.commands.KartsCommand;
import es.allblue.lizardon.commands.CombateCommand;
import es.allblue.lizardon.commands.Discos;
import es.allblue.lizardon.init.FluidInit;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageConfigServer;
import es.allblue.lizardon.objects.LizardonConfig;
import es.allblue.lizardon.objects.karts.CarreraManager;
import net.minecraft.block.BlockState;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.server.command.ConfigCommand;

import java.io.BufferedReader;
import java.io.File;
import java.util.UUID;


@Mod.EventBusSubscriber
public class LizardonEvents {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.PlayerTickEvent event) {
        /*
        if (event.phase == TickEvent.Phase.START) {
            BlockState bloque = event.player.level.getBlockState(event.player.blockPosition());
            if(bloque.getBlock() == FluidInit.AGUAS_TERMALES_BLOCK.get() && !event.player.hasEffect(Effects.REGENERATION)){
                event.player.addEffect(new net.minecraft.potion.EffectInstance(Effects.REGENERATION, 100, 1));
                }
            }*/
        }

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event){

        try {
            Lizardon.carreraManager = new CarreraManager();
        }catch (NullPointerException e){
            System.out.println("ERROR AL CREAR EL CARRERA MANAGER");
        }
    }


    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        new TestCommand(event.getDispatcher());
        new Discos(event.getDispatcher());
        new CombateCommand(event.getDispatcher());

        new KartsCommand(event.getDispatcher());

        /*
        if(event.getEnvironment().compareTo(Commands.EnvironmentType.DEDICATED) == 0){
            new KartsCommand(event.getDispatcher());
        }*/

        ConfigCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent ev){
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) ev.getPlayer();

        boolean inicio = serverPlayer.getPersistentData().getBoolean("inicio");

        //((ServerPlayerEntity) ev.getPlayer()).sendMessage(new StringTextComponent(LizardonConfig.test.get()), UUID.randomUUID());

        Gson gson = new Gson();
        File file = new File("config/lizardon.json");
        LizardonConfig config;
        try{
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            config = gson.fromJson(br, LizardonConfig.class);

            String data = gson.toJson(config);
            System.out.println("DATOS CONFIG ENVIADOS: " + data);
            Lizardon.config = config;
            Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) ev.getPlayer()), new CMessageConfigServer(data));


        }catch(Exception e){
            System.out.println("ERROR AL LEER EL ARCHIVO");
            System.out.println(e);
        }
    }


}
