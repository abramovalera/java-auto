import java.util.Scanner;

public class FigureAreas {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        AreaFigure[] figures = new AreaFigure[n];
        for (int i = 0; i < n; i++) {
            String type = in.next();
            if (type.equals("circle")) {
                figures[i] = new AreaCircle(in.nextInt());
            } else if (type.equals("rect")) {
                figures[i] = new AreaRectangle(in.nextInt(), in.nextInt());
            } else {
                figures[i] = new AreaSquare(in.nextInt());
            }
        }
        double total = 0;
        for (AreaFigure f : figures) {
            System.out.println(f.describe());
            total += f.area();
        }
        System.out.println("Всего фигур: " + n + ", общая площадь: " + AreaFigure.round2(total));
    }
}

abstract class AreaFigure {
    private final String title;

    AreaFigure(String title) {
        this.title = title;
    }

    abstract double area();

    String describe() {
        return title + ": " + round2(area());
    }

    static double round2(double x) {
        return Math.round(x * 100) / 100.0;
    }
}

class AreaCircle extends AreaFigure {
    private final int radius;

    AreaCircle(int radius) {
        super("Круг");
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class AreaRectangle extends AreaFigure {
    private final int width;
    private final int height;

    AreaRectangle(int width, int height) {
        super("Прямоугольник");
        this.width = width;
        this.height = height;
    }

    @Override
    double area() {
        return width * height;
    }
}

class AreaSquare extends AreaFigure {
    private final int side;

    AreaSquare(int side) {
        super("Квадрат");
        this.side = side;
    }

    @Override
    double area() {
        return side * side;
    }
}
