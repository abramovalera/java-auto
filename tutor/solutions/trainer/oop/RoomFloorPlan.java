import java.util.Scanner;

public class RoomFloorPlan {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int width = scanner.nextInt();
        int length = scanner.nextInt();
        FloorRoom room = new FloorRoom(width, length);
        System.out.println("Площадь: " + room.area());
        System.out.println("Периметр: " + room.perimeter());
        System.out.println("Квадрат: " + room.isSquare());
    }
}

class FloorRoom {
    int width;
    int length;

    FloorRoom(int width, int length) {
        this.width = width;
        this.length = length;
    }

    int area() {
        return width * length;
    }

    int perimeter() {
        return 2 * (width + length);
    }

    boolean isSquare() {
        return width == length;
    }
}
