package es.boffmedia.teras;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.boffmedia.teras.util.data.WingullAPI;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.ShapeProperties;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Type;
import java.util.*;

public class PolygonCreator {
    private static final Map<String, PolygonOverlay> polygonMap = new HashMap<>();

    public static void createPolygon() {
        IClientAPI jmAPI = ClientAPI.INSTANCE;

        if (jmAPI != null) {
            Teras.getLogger().info("jmAPI is not null");

            for (Region region : Teras.regions) {
                String regionName = convertToTitleCase(region.getName());
                List<BlockPos> points = new ArrayList<>();

                for (Point point : region.getPoints()) {
                    points.add(new BlockPos(point.getX(), 64, point.getZ()));
                }

                if(points.size() < 3){
                    Teras.getLogger().info("Region " + regionName + " has less than 3 points, skipping");
                    continue;
                }

                ShapeProperties shapeProperties = new ShapeProperties();
                shapeProperties.setFillColor(region.getFillColor());
                shapeProperties.setStrokeColor(region.getStrokeColor());

                MapPolygon polygon = new MapPolygon(points);
                RegistryKey<World> dimension = World.OVERWORLD;
                PolygonOverlay overlay = new PolygonOverlay("journeymap", region.getName(), dimension, shapeProperties, polygon);
                overlay.setTitle(regionName);
                overlay.setLabel(regionName);

                overlay.setActiveUIs(EnumSet.of(Context.UI.Fullscreen));



                try {
                    jmAPI.show(overlay);
                    polygonMap.put(region.getName(), overlay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void removePolygon(String name) {
        IClientAPI jmAPI = ClientAPI.INSTANCE;
        if (jmAPI != null) {
            PolygonOverlay overlay = polygonMap.remove(name);
            if (overlay != null) {
                try {
                    jmAPI.remove(overlay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void removeAll(){
        IClientAPI jmAPI = ClientAPI.INSTANCE;
        if (jmAPI != null) {
            for (Map.Entry<String, PolygonOverlay> entry : polygonMap.entrySet()) {
                try {
                    jmAPI.remove(entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            polygonMap.clear();
        }
    }


    public static class Region {
        private String name;
        private List<Point> points;
        private int fillColor;
        private int strokeColor;

        public String getName() {
            return name;
        }

        public List<Point> getPoints() {
            return points;
        }

        public int getFillColor() {
            return fillColor;
        }

        public int getStrokeColor() {
            return strokeColor;
        }
    }

    private static class Point {
        private int x;
        private int z;

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }
    }

    public static String convertToTitleCase(String snakeCase) {
        if (!snakeCase.contains("_") || "__global__".equals(snakeCase)) {
            return snakeCase;
        }

        String[] parts = snakeCase.split("_");
        StringBuilder titleCase = new StringBuilder();
        for (String part : parts) {
            titleCase.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase())
                    .append(" ");
        }
        return titleCase.toString().trim();
    }

}