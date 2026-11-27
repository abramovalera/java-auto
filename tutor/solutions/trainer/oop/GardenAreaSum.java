import java.util.Scanner;

public class GardenAreaSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        GardenShape[] beds = new GardenShape[n];
        for (int i = 0; i < n; i++) {
            String kind = scanner.next();
            if (kind.equals("circle")) {
                beds[i] = new GardenCircle(scanner.nextDouble());
            } else if (kind.equals("rect")) {
                beds[i] = new GardenRectangle(scanner.nextDouble(), scanner.nextDouble());
            } else {
                beds[i] = new GardenTriangle(scanner.nextDouble(), scanner.nextDouble());
            }
        }
        double total = 0;
        for (GardenShape bed : beds) {
            System.out.println(bed.title() + ": " + Math.round(bed.area() * 100) / 100.0);
            total += bed.area();
        }
        System.out.println("Всего: " + Math.round(total * 100) / 100.0);
    }
}

interface GardenShape {
    double area();

    String title();
}

class GardenCircle implements GardenShape {
    private final double radius;

    GardenCircle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String title() {
        return "Круг";
    }
}

class GardenRectangle implements GardenShape {
    private final double width;
    private final double height;

    GardenRectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String title() {
        return "Прямоугольник";
    }
}

class GardenTriangle implements GardenShape {
    private final double base;
    private final double height;

    GardenTriangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public double area() {
        return base * height / 2;
    }

    @Override
    public String title() {
        return "Треугольник";
    }
}
