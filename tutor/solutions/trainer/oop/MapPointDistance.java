import java.util.Scanner;

public class MapPointDistance {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MapPoint a = new MapPoint(scanner.nextDouble(), scanner.nextDouble());
        MapPoint b = new MapPoint(scanner.nextDouble(), scanner.nextDouble());
        double d = a.distanceTo(b);
        System.out.println("Расстояние: " + Math.round(d * 100) / 100.0);
        System.out.println("Середина: " + a.midpoint(b));
    }
}

class MapPoint {
    double x;
    double y;

    MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double distanceTo(MapPoint other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    MapPoint midpoint(MapPoint other) {
        return new MapPoint((x + other.x) / 2, (y + other.y) / 2);
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
}
