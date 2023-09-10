package es.allblue.lizardon.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import es.allblue.lizardon.objects.pixelmon.frentebatalla.GetEquipo;
import es.allblue.lizardon.util.FileHelper;
import es.allblue.lizardon.util.MessageHelper;
import es.allblue.lizardon.util.PersistentDataFields;
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
            MessageHelper.enviarMensaje(player, "Ya tienes un equipo guardado, usa /cargarEquipo " + equipo + " para cargarlo");
        }
        PlayerPartyStorage storage = StorageProxy.getParty(player.getUUID());
        List<Pokemon> team = storage.getTeam();


        MessageHelper.enviarMensaje(player, "Tienes " + storage.getTeam().size() + " pokemon en tu equipo");
        CompoundNBT nbt = new CompoundNBT();
        HashMap<Integer, String> lista = new HashMap<>();
        for (int i = 0; i < team.size(); i++) {
            Pokemon pokemon = team.get(i);
            CompoundNBT pknbt = new CompoundNBT();
            pokemon.writeToNBT(pknbt);

            nbt.put(i+"", pknbt);
        }

        FileHelper.writeNBT( "lizardon/data/" + player.getUUID() + "/"+ equipo +".dat", nbt);

        MessageHelper.enviarMensaje(player, "Se ha guardado tu equipo en lizardon/data/" + player.getUUID() + "/" + equipo + ".dat");

        /*
        for(int i = 0; i < team.size(); i++) {
            storage.set(i, null);
        }*/
    }



}
