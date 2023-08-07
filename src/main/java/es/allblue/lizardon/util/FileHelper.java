package es.allblue.lizardon.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.LizardonConfig;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.lang.reflect.Type;
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


    public static Object readFile(String ruta, Type token) {
        return readFile(new File(ruta), token);
    }

    private static Object readFile(File file, Type token) {
        Gson gson = new Gson();
        try {
            if(!file.exists()){
                file.createNewFile();
                Object o = token.getClass().newInstance();
                writeFile(file, o);
                return o;
            } else{
                BufferedReader reader = Files.newBufferedReader(Paths.get(file.getPath()));
                return gson.fromJson(reader, token);
            }
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
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

    public static LizardonConfig getConfig() {
        Gson gson = new Gson();
        File file = new File("config/lizardon.json");
        LizardonConfig config;
        try{
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            config = gson.fromJson(br, LizardonConfig.class);
            Lizardon.config = config;
            return config;
        } catch (FileNotFoundException e) {
            config = new LizardonConfig();
            config.setId(RandomStringUtils.random(8, true, true));
            config.setHome("http://localhost:8080");
            config.setAPI_URL("http://localhost:3000");

            Lizardon.config = config;
            writeFile(file, config);

            System.out.println("Config file created");
            System.out.println("Please, edit config/lizardon.json with your server data");
            System.out.println("Then, restart the server");
            System.out.println(config);

            return config;
        }

        //return (LizardonConfig) readFile(ruta, LizardonConfig.class);
    }

}
