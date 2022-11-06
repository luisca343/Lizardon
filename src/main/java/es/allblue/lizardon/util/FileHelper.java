package es.allblue.lizardon.util;

import com.google.gson.Gson;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.h2.util.json.JSONArray;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class FileHelper {
    public static void getFile(){
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("/Lizardon/npcs"));
            Map<?, ?> map = gson.fromJson(reader, Map.class);
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }

            // close reader
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(){

    }
}
