import java.util.Scanner;

public class GridPointEquals {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GridPoint a = new GridPoint(scanner.nextInt(), scanner.nextInt());
        GridPoint b = new GridPoint(scanner.nextInt(), scanner.nextInt());
        System.out.println("a = " + a + ", b = " + b);
        System.out.println("a == b: " + (a == b));
        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("Хеш-коды равны: " + (a.hashCode() == b.hashCode()));
        System.out.println("a.equals(null): " + a.equals(null));
    }
}

class GridPoint {
    private final int x;
    private final int y;

    GridPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GridPoint)) {
            return false;
        }
        GridPoint other = (GridPoint) o;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
