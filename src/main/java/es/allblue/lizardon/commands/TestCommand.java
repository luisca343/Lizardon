package es.allblue.lizardon.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreenPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientChangeOpenPCPacket;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.objects.pixelmon.PokemonData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TestCommand {
    private static VoicechatServerApi SERVER_API;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("lizartest")
                .executes((command) -> {
                    return testCommand(command.getSource());
        }));
    }

    public int testCommand(CommandSource source) {
        try{
            ServerPlayerEntity player = source.getPlayerOrException();
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
            System.out.println(str);

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
