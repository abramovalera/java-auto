import java.util.Scanner;

public class TreasureMapDistance {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TreasurePoint start = new TreasurePoint(sc.nextInt(), sc.nextInt());
        TreasurePoint treasure = new TreasurePoint(sc.nextInt(), sc.nextInt());

        double d = start.distanceTo(treasure);
        double rounded = Math.round(d * 100) / 100.0;
        System.out.println("Расстояние от " + start.describe() + " до " + treasure.describe() + ": " + rounded);
    }
}

class TreasurePoint {
    private final int x;
    private final int y;

    TreasurePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    String describe() {
        return "(" + x + ", " + y + ")";
    }

    double distanceTo(TreasurePoint other) {
        int dx = other.x - x;
        int dy = other.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
