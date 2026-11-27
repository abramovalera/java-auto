import java.util.Objects;
import java.util.Scanner;

public class PointEqualsDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EqualsPoint a = new EqualsPoint(scanner.nextInt(), scanner.nextInt());
        EqualsPoint b = new EqualsPoint(scanner.nextInt(), scanner.nextInt());

        System.out.println("A = " + a);
        System.out.println("B = " + b);
        System.out.println("A == B: " + (a == b));
        System.out.println("A.equals(B): " + a.equals(b));
        System.out.println("B.equals(A): " + b.equals(a));
        if (a.equals(b)) {
            System.out.println("hashCode совпадает: " + (a.hashCode() == b.hashCode()));
        }
        System.out.println("A.equals(null): " + a.equals(null));
        System.out.println("A.equals(строка): " + a.equals(a.toString()));
    }
}

class EqualsPoint {
    private final int x;
    private final int y;

    EqualsPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EqualsPoint other = (EqualsPoint) o;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
