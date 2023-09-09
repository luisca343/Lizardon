package es.allblue.lizardon.event;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.blocks.tileentity.PCTileEntity;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.blocks.TestModeloFunko;
import es.allblue.lizardon.commands.*;
import es.allblue.lizardon.init.FluidInit;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageConfigServer;
import es.allblue.lizardon.objects.LizardonConfig;
import es.allblue.lizardon.objects.karts.CarreraManager;
import es.allblue.lizardon.util.FileHelper;
import es.allblue.lizardon.util.MessageUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
    public static void modelBake(ModelBakeEvent event) {
        System.out.println("modelBake");
        ModelResourceLocation mrl = new ModelResourceLocation("lizardon:funko", "inventory");
        IBakedModel model = event.getModelRegistry().get(mrl);
        event.getModelRegistry().put(mrl, new TestModeloFunko(model));
    }

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
        new DiscosCommand(event.getDispatcher());
        new CombateCommand(event.getDispatcher());

        new KartsCommand(event.getDispatcher());

        /*
        if(event.getEnvironment().compareTo(Commands.EnvironmentType.DEDICATED) == 0){
            new KartsCommand(event.getDispatcher());
        }*/

        ConfigCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onTeleport(PlayerEvent.PlayerChangedDimensionEvent event){
        if(event.getPlayer().getPersistentData().getBoolean("frentebatalla")){
            event.setCanceled(true);
            MessageUtil.enviarMensaje(event.getPlayer(), "NO PUEDES TELETRANSPORTARTE EN ESTE MOMENTO");
        }
    }

    @SubscribeEvent
    public static void test(PlayerInteractEvent.RightClickBlock event){
        TileEntity block = event.getWorld().getBlockEntity(event.getPos());
        TileEntity blockDebajo = event.getWorld().getBlockEntity(event.getPos().below());
        if(!event.getPlayer().getPersistentData().getBoolean("frentebatalla")) return;


        if(block instanceof PCTileEntity || blockDebajo instanceof PCTileEntity){
            MessageUtil.enviarMensaje(event.getPlayer(), "NO PUEDES USAR EL PC EN ESTE MOMENTO");
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){

    }



    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent ev){
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) ev.getPlayer();

        boolean inicio = serverPlayer.getPersistentData().getBoolean("inicio");


        //((ServerPlayerEntity) ev.getPlayer()).sendMessage(new StringTextComponent(LizardonConfig.test.get()), UUID.randomUUID());

        Gson gson = new Gson();
        LizardonConfig lizardonConfig = FileHelper.getConfig();
        String data = gson.toJson(lizardonConfig);
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) ev.getPlayer()), new CMessageConfigServer(data));

    }


}
