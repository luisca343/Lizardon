package es.boffmedia.teras.util;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import es.boffmedia.teras.api.PokePasteReader;
import es.boffmedia.teras.objects.pixelmon.ConfigCombate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Reader {
    public static InputStream getConnectionStream(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/4.0");

            return con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ConfigCombate getDatosEncuentro(String npc) {
        return getDatosCombate(npc, "eventos");
    }


    public static ConfigCombate getDatosNPC(String npc) {
        return getDatosCombate(npc, "entrenadores");
    }

    public static ConfigCombate getDatosCombate(String npc, String tipo) {
        URL url = null;
        try {
            url = new URL("http://i.teras.es/pixelmon/combates/"+ tipo +"/"+npc+".json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream inputStream = getConnectionStream(url);

        if(inputStream == null) {
            return null;
        }

        String str = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

        ConfigCombate configCombate = new Gson().fromJson(str, ConfigCombate.class);

        List<Pokemon> team = PokePasteReader.fromTeras(tipo +"/"+npc).build();

        configCombate.setEquipo(team);
        configCombate.setNombreArchivo(npc);
        configCombate.setCarpeta(tipo);


        return configCombate;
    }
}
