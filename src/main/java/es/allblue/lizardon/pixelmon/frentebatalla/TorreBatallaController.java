package es.allblue.lizardon.pixelmon.frentebatalla;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import es.allblue.lizardon.api.PokePasteReader;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageRunJS;
import es.allblue.lizardon.pixelmon.battle.Combate;
import es.allblue.lizardon.objects.pixelmon.ConfigCombate;
import es.allblue.lizardon.pixelmon.battle.CombateFrenteBatalla;
import es.allblue.lizardon.pixelmon.battle.LizardonBattleController;
import es.allblue.lizardon.util.MessageHelper;
import es.allblue.lizardon.util.Reader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TorreBatallaController {



    public static void registrarEquipo(LizardonBattleController.TipoCombate tipo, ServerPlayerEntity player){
        String mensaje = String.format("frenteBatalla('%s', '%s')", tipo, player.getUUID());
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CMessageRunJS(mensaje));

    }

    public static void iniciarCombate(ServerPlayerEntity player){

        List<Pokemon> tier1 = PokePasteReader.fromLizardon("Frente Batalla/tier1").build();
        List<Pokemon> equipo = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int num = new Random().nextInt(tier1.size());
            System.out.println(i + " " + num);
            MessageHelper.enviarMensaje(player, "§aCargando pokemon " + tier1.get(num).getSpecies().getName());
            Pokemon pokemon = tier1.get(num);

            boolean match = equipo.stream().anyMatch(p -> p.getSpecies().getName().equals(pokemon.getSpecies().getName()));
            if(match){
                i--;
                continue;
            }

            tier1.remove(num);
            equipo.add(pokemon);
        }

        ConfigCombate configCombate = new ConfigCombate();
        configCombate.setEquipo(equipo);
        configCombate.setCarpeta("torre_batalla");
        configCombate.setNombreArchivo("tier1");
        configCombate.setNombre("Tier 1");
        configCombate.setDinero(0);
        configCombate.setCurar(true);
        configCombate.setExp(false);
        configCombate.setNombre("Entrenador de la Torre Batalla");


        CombateFrenteBatalla combate = new CombateFrenteBatalla(player, configCombate);
        combate.iniciarCombate();
    }


    public static void iniciarCombatev2(ServerPlayerEntity player) {
        List<Pokemon> tier1 = PokePasteReader.fromLizardon("Frente Batalla/tier1").build();
        List<Pokemon> equipo = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int num = new Random().nextInt(tier1.size());
            System.out.println(i + " " + num);
            MessageHelper.enviarMensaje(player, "§aCargando pokemon " + tier1.get(num).getSpecies().getName());
            Pokemon pokemon = tier1.get(num);

            boolean match = equipo.stream().anyMatch(p -> p.getSpecies().getName().equals(pokemon.getSpecies().getName()));
            if(match){
                i--;
                continue;
            }

            tier1.remove(num);
            equipo.add(pokemon);
        }

        ConfigCombate configCombate = new ConfigCombate();
        configCombate.setEquipo(equipo);
        configCombate.setCarpeta("torre_batalla");
        configCombate.setNombreArchivo("tier1");
        configCombate.setNombre("Tier 1");
        configCombate.setDinero(0);
        configCombate.setCurar(true);
        configCombate.setExp(false);
        configCombate.setNombre("Entrenador de la Torre Batalla");


        CombateFrenteBatalla combate = new CombateFrenteBatalla(player, configCombate);
        combate.iniciarCombate();
    }
}


