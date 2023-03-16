package es.allblue.lizardon.commands;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrcrayfish.vehicle.VehicleMod;
import com.mrcrayfish.vehicle.entity.EngineTier;
import com.mrcrayfish.vehicle.entity.VehicleEntity;
import com.mrcrayfish.vehicle.entity.VehicleProperties;
import com.mrcrayfish.vehicle.entity.vehicle.GoKartEntity;
import com.mrcrayfish.vehicle.init.ModEntities;
import de.maxhenkel.voicechat.api.Player;
import de.maxhenkel.voicechat.api.ServerLevel;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageDatosServer;
import es.allblue.lizardon.net.client.CMessageVerVideo;
import es.allblue.lizardon.objects.pixelmon.PokemonData;
import es.allblue.lizardon.util.AudioManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Discos {
    private static VoicechatServerApi SERVER_API;
    public  static FolderName DISCOS = new FolderName("discos");
    public static AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000F, 16, 1, 2, 48000F, false);

    public Discos(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("discotest")
                .then(Commands.argument("url", StringArgumentType.string())
                .then(Commands.argument("nombre", StringArgumentType.string())
            .executes((command) -> {
                String url = command.getArgument("url", String.class);
                String nombre = command.getArgument("nombre", String.class);

                AudioManager manager = new AudioManager();
                try {
                    AudioManager.guardar(url, nombre, command.getSource().getLevel());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //manager.play(command.getSource().getLevel());

              return 1;
            })
        )));
    }

    public int testCommand(CommandContext ctx) {
        try{

            String url = (String) ctx.getArgument("url", String.class);
            CommandSource source = (CommandSource) ctx.getSource();
            ServerPlayerEntity player = source.getPlayerOrException();

            Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageVerVideo(url));
            /*
            ServerPlayerEntity player = source.getPlayerOrException();
            World world = player.level;
            GoKartEntity vehicleEntity = new GoKartEntity(ModEntities.GO_KART.get(), world);
            vehicleEntity.setRequiresFuel(false);
            vehicleEntity.setColorRGB(1,2,3);
            vehicleEntity.setEngine(true);
            vehicleEntity.setEngineTier(EngineTier.WOOD);
            vehicleEntity.setPos(player.getX(), player.getY(), player.getZ());
            vehicleEntity.setOwner(player.getUUID());
            vehicleEntity.getSeatTracker().setSeatIndex(0, player.getUUID());
            world.addFreshEntity(vehicleEntity);

            player.startRiding(vehicleEntity);*/


            /*
            PlayerPartyStorage storage = StorageProxy.getParty(player);
            PCStorage pc = StorageProxy.getPCForPlayer(player);

            HashMap<Integer, HashMap<Integer, PokemonData>> cajas = new HashMap<>();
            for (PCBox caja : pc.getBoxes()) {
                HashMap<Integer, PokemonData> cajaActual = new HashMap<>();
                for(int i = 0; i < 30; i++){
                    Pokemon pkm = caja.get(i);
                    if(pkm != null) {
                        PokemonData data = new PokemonData();
                    }
                }
                cajas.put(caja.boxNumber, cajaActual);
            }


            Gson gson = new Gson();
            String str = gson.toJson(cajas);
            System.out.println(str);*/

            /*
            Pokemon pokemon = storage.get(0);
            System.out.println("ENCONTRADO:" + pokemon.getSpecies().getName());
            pokemon.writeToNBT(nbt);
            System.out.println(nbt);

            Pokemon clon = PokemonFactory.create(nbt);
            storage.set(2, clon);*/
        }catch(Exception e){
            e.printStackTrace();
        }

        return 1;
    }

}
