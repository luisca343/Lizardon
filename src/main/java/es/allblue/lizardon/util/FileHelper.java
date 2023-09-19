package es.allblue.lizardon.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.serverdata.LizardonConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileHelper {
    public static boolean writeFile(String ruta, Object o) {
        String folder = ruta.substring(0, ruta.lastIndexOf("/"));
        File folderFile = new File(folder);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }
        return writeFile(new File(ruta), o);
    }

    public static boolean writeStringFile(String ruta, String o) {
        String folder = ruta.substring(0, ruta.lastIndexOf("/"));
        File folderFile = new File(folder);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }
        return writeStringFile(new File(ruta), o);
    }


    public static boolean writeStringFile(File file, String o) {
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            Files.write(file.toPath(), o.getBytes(Charsets.UTF_8));
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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

    public static Map getMapFromFile(String ruta) {
        Object o = readFile(ruta, Map.class);
        if(o == null){
            return new HashMap();
        }
        return (Map) o;
    }

    public static void writeNBT( String ruta, CompoundNBT nbt){
        try {
            CompressedStreamTools.write(nbt, new File(ruta));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompoundNBT readNBT(String ruta){
        try {
            return CompressedStreamTools.read(new File(ruta));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteFile(String ruta){
        return new File(ruta).delete();
    }

    public static boolean exists(String ruta){
        return new File(ruta).exists();
    }

    public static Object readFile(String ruta, Type token) {
        return readFile(new File(ruta), token);
    }

    private static <T> T readFile(File file, Type token) {
        Gson gson = new Gson();
        try {
            if(!file.exists()){
                file.createNewFile();
                Object o = token.getClass().newInstance();
                writeFile(file, o);
                return (T) o;
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
    }

    public static File getSchematic(String name) {
        File file = new File("plugins/WorldEdit/schematics/" + name + ".schematic");
        if(!file.exists()){
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

}
