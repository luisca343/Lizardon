package es.allblue.lizardon.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.client.CMessageDatosServer;
import es.allblue.lizardon.objects.DatosServer;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {
    public static boolean writeFile(String ruta, Object o) {
        String folder = ruta.substring(0, ruta.lastIndexOf("/"));
        File folderFile = new File(folder);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }
        return writeFile(new File(ruta), o);
    }

    public static boolean writeFile(File file, Object o) {
        Gson gson = new Gson();
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charsets.UTF_8);
            gson.toJson(o, writer);
            writer.flush();
            writer.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static Object readFile(String ruta, Class<?> clazz) {
        return readFile(new File(ruta), clazz);
    }

    public static Object readFile(File file, Class<?> clazz) {
        Gson gson = new Gson();
        try {
            if(!file.exists()){
                file.createNewFile();
                Object o = clazz.newInstance();
                writeFile(file, o);
                return o;
            } else{
                BufferedReader reader = Files.newBufferedReader(Paths.get(file.getPath()));
                return gson.fromJson(reader, clazz);
            }
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
