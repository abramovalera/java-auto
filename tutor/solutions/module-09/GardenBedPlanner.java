import java.util.Scanner;

public class GardenBedPlanner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int width = sc.nextInt();
        int length = sc.nextInt();

        GardenBed bed = new GardenBed(width, length);
        System.out.println("Грядка " + bed.getWidth() + " x " + bed.getLength());
        System.out.println("Площадь: " + bed.area());
        System.out.println("Забор: " + bed.perimeter());
    }
}

class GardenBed {
    private final int width;
    private final int length;

    GardenBed(int width, int length) {
        this.width = width;
        this.length = length;
    }

    int getWidth() {
        return width;
    }

    int getLength() {
        return length;
    }

    int area() {
        return width * length;
    }

    int perimeter() {
        return 2 * (width + length);
    }
}
