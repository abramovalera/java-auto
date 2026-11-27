import java.util.Scanner;

public class SquareGardenMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int side = scanner.nextInt();
        System.out.println("Площадь клумбы: " + square(side));
    }

    static int square(int side) {
        return side * side;
    }
}
