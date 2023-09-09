package es.allblue.lizardon.pixelmon.frentebatalla;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import es.allblue.lizardon.api.PokePasteReader;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageRunJS;
import es.allblue.lizardon.net.client.CMessageVerVideo;
import es.allblue.lizardon.objects.pixelmon.Combate;
import es.allblue.lizardon.objects.pixelmon.ConfigCombate;
import es.allblue.lizardon.pixelmon.battle.LizardonBattleController;
import es.allblue.lizardon.util.MessageUtil;
import es.allblue.lizardon.util.Reader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
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
        List<Pokemon> tier1 = PokePasteReader.fromLizardon("frente batalla/tier1").build();
        List<Pokemon> equipo = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int num = new Random().nextInt(tier1.size());
            System.out.println(i + " " + num);
            MessageUtil.enviarMensaje(player, "§aCargando pokemon " + tier1.get(num).getSpecies().getName());
            Pokemon pokemon = tier1.get(num);

            boolean match = equipo.stream().anyMatch(p -> p.getDisplayName().equals(pokemon.getDisplayName()));
            if(match){
                i--;
                continue;
            }

            tier1.remove(num);
            equipo.add(pokemon);
        }

        ConfigCombate configCombate = new ConfigCombate();
        System.out.println(equipo.size());
        System.out.println(equipo);
        configCombate.setEquipo(equipo);
        configCombate.setCarpeta("torre_batalla");
        configCombate.setNombreArchivo("tier1");
        configCombate.setNombre("Tier 1");
        configCombate.setDinero(0);



        Combate combate = new Combate(player, configCombate);
        combate.iniciarCombate();
    }



    public static ConfigCombate getDatosCombate(String npc, String tipo) {

        URL url = null;
        try {
            url = new URL("http://i.lizardon.es/pixelmon/combates/"+ tipo +"/"+npc+".json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream inputStream = Reader.getConnectionStream(url);

        if(inputStream == null) {
            return null;
        }

        String str = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

        ConfigCombate configCombate = new Gson().fromJson(str, ConfigCombate.class);

        List<Pokemon> team = PokePasteReader.fromLizardon(tipo +"/"+npc).build();

        configCombate.setEquipo(team);
        configCombate.setNombreArchivo(npc);
        configCombate.setCarpeta(tipo);


        return configCombate;
    }






}


