package es.allblue.lizardon.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import es.allblue.lizardon.objects.pixelmon.frentebatalla.GetEquipo;
import es.allblue.lizardon.util.FileHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public class TeamManager {
    public static List<Pokemon> getCurrentTeam(ServerPlayerEntity player){
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());
        return storage.getTeam();
    }

    public static List<Pokemon> getTeam(ServerPlayerEntity player, String file){
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());
        CompoundNBT nbt = FileHelper.readNBT("lizardon/data/" + player.getUUID() + "/" + file + ".dat");

        for(int i = 0; i < nbt.size(); i++) {
            CompoundNBT pknbt = nbt.getCompound(i+"");
            Pokemon pokemon = PokemonFactory.create(pknbt);
            storage.set(i, pokemon);
        }

        FileHelper.deleteFile("lizardon/data/" + player.getUUID() + "/" + file + ".dat");
        return storage.getTeam();
    }

    public static void deleteTeam(ServerPlayerEntity player, String file){
        FileHelper.deleteFile("lizardon/data/" + player.getUUID() + "/" + file + ".dat");
    }

    public static void saveTeam(ServerPlayerEntity player, String file){
        CompoundNBT nbt = new CompoundNBT();
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());
        List<Pokemon> team = storage.getTeam();
        for(int i = 0; i < team.size(); i++) {
            CompoundNBT pknbt = team.get(i).writeToNBT(new CompoundNBT());
            nbt.put(i+"", pknbt);
        }
        FileHelper.writeNBT("lizardon/data/" + player.getUUID() + "/"+file+".dat", nbt);
    }


    public static void loadFromSlots(ServerPlayerEntity player, List<GetEquipo.PkmSlot> slots) {
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());
        List<Pokemon> team = storage.getTeam();

        for(int i = 0; i < 6; i++) {
            if(i >= slots.size()){
                storage.set(i, null);
                continue;
            }
            GetEquipo.PkmSlot slot = slots.get(i);
            Pokemon pkm;
            if(slot.getCaja() == -1){
                pkm = team.get(slot.getSlot());
            } else {
                PCStorage pc = StorageProxy.getPCForPlayer(player);
                pkm = pc.get(slot.getCaja(), slot.getSlot());
            }

            storage.set(i, pkm);
        }
    }


    public static void loadTeam(ServerPlayerEntity player, String file){
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());
        List<Pokemon> team = getTeam(player, file);

        for(int i = 0; i < 6; i++) {
            if(i >= team.size()){
                storage.set(i, null);
                continue;
            }
            storage.set(i, team.get(i));
        }
    }
}
