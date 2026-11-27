import java.util.Scanner;

public class PhotoFrameSizes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        int side = scanner.nextInt();
        PhotoFrame first = new PhotoFrame(width, height);
        PhotoFrame second = new PhotoFrame(side);
        System.out.println("Рамка " + first + ", площадь " + first.area());
        System.out.println("Рамка " + second + ", площадь " + second.area());
        System.out.println("Первая больше: " + first.biggerThan(second));
    }
}

class PhotoFrame {
    int width;
    int height;

    PhotoFrame(int width, int height) {
        this.width = width;
        this.height = height;
    }

    PhotoFrame(int side) {
        this(side, side);
    }

    int area() {
        return width * height;
    }

    boolean biggerThan(PhotoFrame other) {
        return area() > other.area();
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
