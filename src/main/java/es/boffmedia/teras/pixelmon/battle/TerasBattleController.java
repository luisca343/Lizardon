package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import es.boffmedia.teras.util.FileHelper;
import es.boffmedia.teras.util.MessageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.List;

public class TerasBattleController {
    public HashMap<Integer, TerasBattle> terasBattles = new HashMap<>();



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


    public boolean existsTerasBattle(int id){
        return terasBattles.containsKey(id);
    }

    public TerasBattle getTerasBattle(int id){
        return terasBattles.get(id);
    }

    public void addTerasBattle(int id, TerasBattle combate){
        terasBattles.put(id, combate);
    }

    public void removeTerasBattle(int id){
        terasBattles.remove(id);
    }

    /*
    public boolean existeEquipo(PlayerEntity player, String equipo){
        return FileHelper.exists("teras/data/" + player.getUUID() + "/" + equipo + ".dat");
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

        FileHelper.writeNBT( "teras/data/" + player.getUUID() + "/"+ equipo +".dat", nbt);

        MessageHelper.enviarMensaje(player, "Se ha guardado tu equipo en teras/data/" + player.getUUID() + "/" + equipo + ".dat");
        */
        /*
        for(int i = 0; i < team.size(); i++) {
            storage.set(i, null);
        }
    }*/



}
