package es.boffmedia.teras.util.objects.karts;

public class Checkpoint {
    CoordinatePoint start;
    CoordinatePoint end;

    public Checkpoint(CoordinatePoint start, CoordinatePoint end) {
        this.start = start;
        this.end = end;
    }

    public CoordinatePoint getStart() {
        return start;
    }

    public CoordinatePoint getEnd() {
        return end;
    }

    public void setStart(CoordinatePoint start) {
        this.start = start;
    }

    public void setEnd(CoordinatePoint end) {
        this.end = end;
    }

    public boolean equals(Checkpoint checkpoint) {
        return start.equals(checkpoint.getStart()) && end.equals(checkpoint.getEnd());
    }

    public boolean isInCheckpoint(CoordinatePoint loc){
        CoordinatePoint l1 = start;
        CoordinatePoint l2 = end;

        int playerX = (int) loc.getX();
        int playerY = (int) loc.getY();
        int playerZ = (int) loc.getZ();

        double x1 = Math.min(l1.getX(), l2.getX()) -1;
        double x2 = Math.max(l1.getX(), l2.getX()) +1;

        double y1 = Math.min(l1.getY(), l2.getY()) -1;
        double y2 = Math.min(l1.getY(), l2.getY()) +1;

        double z1 = Math.min(l1.getZ(), l2.getZ()) -1;
        double z2 = Math.max(l1.getZ(), l2.getZ()) +1;

        return playerX >= x1 && playerX <= x2
                && playerY >= y1 && playerY <= y2
                && playerZ >= z1 && playerZ <= z2;

        /*
        Teras.getLogger().info("==================================");
        Teras.getLogger().info("En Checkpoint: " + enCheck);
        Teras.getLogger().info("Posicion actual: " + loc);
        Teras.getLogger().info("CheckPoint: " + l1 + " - " + l2);
        return enCheck;*/
    }

    public String toString() {
        return "Start: " + start.toString() + ", End: " + end.toString();
    }
}
