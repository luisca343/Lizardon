package es.allblue.lizardon.net.video;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.karts.Punto;
import es.allblue.lizardon.tileentity.FrameBlockEntity;
import es.allblue.lizardon.util.FileHelper;
import io.leangen.geantyref.TypeToken;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScreenManager {
    public static HashMap<Integer, ArrayList<Punto>> pantallas = new HashMap<>();


    public static void addScreen(BlockPos pos, int canal) {
        Punto punto = new Punto(pos.getX(), pos.getY(), pos.getZ());

        removePunto(punto);

        if (pantallas.containsKey(canal)) {
            pantallas.get(canal).add(punto);
        } else {
            ArrayList<Punto> puntos = new ArrayList<>();
            puntos.add(punto);
            pantallas.put(canal, puntos);
        }

        FileHelper.writeFile("lizardon/pantallas.json", pantallas);
        Lizardon.LOGGER.info("Pantalla añadida: " + pos);
    }

    public static void loadScreens() {

        TypeToken<HashMap<Integer, ArrayList<Punto>>> typeToken = new TypeToken<HashMap<Integer, ArrayList<Punto>>>() {};
        pantallas = (HashMap<Integer, ArrayList<Punto>>) FileHelper.readFile("lizardon/pantallas.json", typeToken.getType());
        if(pantallas == null) pantallas = new HashMap<>();
        Lizardon.LOGGER.info("Pantallas cargadas: " + pantallas.size());
        Lizardon.LOGGER.info("Pantallas: " + pantallas);
    }

    public static void broadcastVideo(String url, int canal, World world) {

        if (pantallas.containsKey(canal)) {
            for (Punto pos : pantallas.get(canal)) {
                BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

                if(world.getBlockEntity(blockPos) == null || !(world.getBlockEntity(blockPos) instanceof FrameBlockEntity)) {
                    Lizardon.LOGGER.info("Pantalla no encontrada: " + pos);
                    removePunto(blockPos);
                    return;
                }
                FrameBlockEntity frame = (FrameBlockEntity) world.getBlockEntity(blockPos);
                frame.broadcastVideo(url);
            }
        }


    }


    public static boolean containsPunto(Punto punto) {
        for (Map.Entry<Integer, ArrayList<Punto>> valor : pantallas.entrySet()) {
            for (Punto punto1 : valor.getValue()) {
                if(punto1.getX() != punto.getX()) continue;
                if(punto1.getY() != punto.getY()) continue;
                if(punto1.getZ() != punto.getZ()) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean removePunto(BlockPos pos) {
        Punto punto = new Punto(pos.getX(), pos.getY(), pos.getZ());
        return removePunto(punto);
    }
    public static boolean removePunto(Punto punto) {
        for (Map.Entry<Integer, ArrayList<Punto>> valor : pantallas.entrySet()) {
            for (Punto punto1 : valor.getValue()) {
                if(punto1.getX() != punto.getX()) continue;
                if(punto1.getY() != punto.getY()) continue;
                if(punto1.getZ() != punto.getZ()) continue;
                valor.getValue().remove(punto1);
                pantallas.put(valor.getKey(), valor.getValue());
                Lizardon.LOGGER.info("Pantalla eliminada: " + punto1);
                return true;
            }
        }
        return false;
    }
}
