package es.allblue.lizardon.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import es.allblue.lizardon.objects.GetEquipo;
import es.allblue.lizardon.objects.pixelmon.Combate;
import es.allblue.lizardon.util.FileHelper;
import es.allblue.lizardon.util.MessageUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LizardonBattleController {


    public static enum TipoCombate{
        INDIVIDUAL("INDIVIDUAL"),
        DOBLE("DOBLE"),
        TRIPLE("TRIPLE"),
        MULTIPLE("MULTIPLE"),
        TB_INDIVIDUAL("TB_INDIVIDUAL"),
        TB_DOBLE("TB_DOBLE"),
        TB_TRIPLE("TB_TRIPLE"),
        TB_MULTIPLE("TB_MULTIPLE");

        public final String label;

        private TipoCombate(String label) {
            this.label = label;
        }
    }

    public HashMap<Integer, Combate> combatesEspeciales = new HashMap<>();

    public boolean existeCombateEspecial(int id){
        return combatesEspeciales.containsKey(id);
    }

    public Combate getCombateEspecial(int id){
        return combatesEspeciales.get(id);
    }

    public void addCombateEspecial(int id, Combate combate){
        combatesEspeciales.put(id, combate);
    }

    public void removeCombateEspecial(int id){
        combatesEspeciales.remove(id);
    }

    public boolean existeEquipo(PlayerEntity player, String equipo){
        return FileHelper.exists("lizardon/data/" + player.getUUID() + "/" + equipo + ".dat");
    }

    public void guardarEquipo(PlayerEntity player, String equipo){
        if(existeEquipo(player, equipo)){
            MessageUtil.enviarMensaje(player, "Ya tienes un equipo guardado, usa /cargarEquipo " + equipo + " para cargarlo");
        }
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());
        List<Pokemon> team = storage.getTeam();


        MessageUtil.enviarMensaje(player, "Tienes " + storage.getTeam().size() + " pokemon en tu equipo");
        CompoundNBT nbt = new CompoundNBT();
        HashMap<Integer, String> lista = new HashMap<>();
        for (int i = 0; i < team.size(); i++) {
            Pokemon pokemon = team.get(i);
            CompoundNBT pknbt = new CompoundNBT();
            pokemon.writeToNBT(pknbt);

            nbt.put(i+"", pknbt);
        }

        FileHelper.writeNBT(nbt, "lizardon/data/" + player.getUUID() + "/"+ equipo +".dat");

        MessageUtil.enviarMensaje(player, "Se ha guardado tu equipo en lizardon/data/" + player.getUUID() + "/" + equipo + ".dat");

        /*
        for(int i = 0; i < team.size(); i++) {
            storage.set(i, null);
        }*/
    }

    public void cargarEquipoBase(PlayerEntity player){
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());
        List<Pokemon> team = storage.getTeam();

        for(int i = 0; i < team.size(); i++) {
            storage.set(i, null);
        }

        CompoundNBT nbt = FileHelper.readNBT("lizardon/data/" + player.getUUID() + "/equipo.dat");

        for(int i = 0; i < nbt.size(); i++) {
            CompoundNBT pknbt = nbt.getCompound(i+"");
            Pokemon pokemon = PokemonFactory.create(pknbt);
            storage.set(i, pokemon);
        }

        player.getPersistentData().putBoolean("frentebatalla", false);
    }

    public void cargarEquipo(PlayerEntity player, String equipo){
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());

        CompoundNBT nbt = FileHelper.readNBT("lizardon/data/" + player.getUUID() + "/" + equipo + ".dat");

        for(int i = 0; i < nbt.size(); i++) {
            CompoundNBT pknbt = nbt.getCompound(i+"");
            Pokemon pokemon = PokemonFactory.create(pknbt);
            storage.set(i, pokemon);
        }

        FileHelper.deleteFile("lizardon/data/" + player.getUUID() + "/" + equipo + ".dat");

        MessageUtil.enviarMensaje(player, "Equipo cargado");
    }

    public void cargarEquipo(PlayerEntity player, CompoundNBT nbt){
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());

        for(int i = 0; i < nbt.size(); i++) {
            CompoundNBT pknbt = nbt.getCompound(i+"");
            Pokemon pokemon = PokemonFactory.create(pknbt);
            storage.set(i, pokemon);
        }
    }
    public void cargarEquipo(PlayerEntity player, List<Pokemon> list){
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());

        for(int i = 0; i < list.size(); i++) {
            Pokemon pokemon = list.get(i);
            storage.set(i, pokemon);
        }
    }

    public List<Pokemon> cargarEquipo(ServerPlayerEntity player) {
        List<Pokemon> team = new ArrayList<>();
        CompoundNBT nbt = FileHelper.readNBT("lizardon/data/" + player.getUUID() + "/equipo.dat");

        for(int i = 0; i < nbt.size(); i++) {
            CompoundNBT pknbt = nbt.getCompound(i+"");
            Pokemon pokemon = PokemonFactory.create(pknbt);
            team.set(i, pokemon);
        }

        return team;
    }

    public void cargarEquipo(ServerPlayerEntity player, List<GetEquipo.PkmSlot> slots) {
        System.out.println("Cargando equipo...");
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

}
