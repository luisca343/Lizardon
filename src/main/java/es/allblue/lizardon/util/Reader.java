package es.allblue.lizardon.util;

import com.google.gson.Gson;
import es.allblue.lizardon.objects.Entrenador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Reader {
    private static InputStream getConnectionStream(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/4.0");

            return con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Entrenador getDatosNPC(String npc) {
        Gson gson = new Gson();
        URL url = null;
        try {
            url = new URL("http://i.lizardon.es/pixelmon/equipos/"+npc+".json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream inputStream = getConnectionStream(url);

        if(inputStream == null) {
            return null;
        }

        String str = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());
        return gson.fromJson(str, Entrenador.class);
    }
}
