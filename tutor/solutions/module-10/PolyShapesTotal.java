import java.util.Scanner;

public class PolyShapesTotal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        PolyShape[] shapes = new PolyShape[n];
        for (int i = 0; i < n; i++) {
            String kind = scanner.next();
            if (kind.equals("circle")) {
                shapes[i] = new PolyCircle(scanner.nextInt());
            } else if (kind.equals("rect")) {
                shapes[i] = new PolyRectangle(scanner.nextInt(), scanner.nextInt());
            } else {
                shapes[i] = new PolySquare(scanner.nextInt());
            }
        }

        double total = 0;
        PolyShape biggest = null;
        for (int i = 0; i < shapes.length; i++) {
            PolyShape shape = shapes[i];
            System.out.println(shape.name() + ": " + round2(shape.area()));
            total += shape.area();
            if (biggest == null || shape.area() > biggest.area()) {
                biggest = shape;
            }
        }
        System.out.println("Сумма площадей: " + round2(total));
        System.out.println("Самая большая: " + (biggest == null ? "нет" : biggest.name()));
    }

    static double round2(double x) {
        return Math.round(x * 100) / 100.0;
    }
}

class PolyShape {
    String name() {
        return "Фигура";
    }

    double area() {
        return 0;
    }
}

class PolyCircle extends PolyShape {
    private final int radius;

    PolyCircle(int radius) {
        this.radius = radius;
    }

    @Override
    String name() {
        return "Круг";
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class PolyRectangle extends PolyShape {
    private final int width;
    private final int height;

    PolyRectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    String name() {
        return "Прямоугольник";
    }

    @Override
    double area() {
        return width * height;
    }
}

class PolySquare extends PolyRectangle {
    PolySquare(int side) {
        super(side, side);
    }

    @Override
    String name() {
        return "Квадрат";
    }
}
