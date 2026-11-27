import java.util.Scanner;

public class CubeBoxCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int side = scanner.nextInt();
        System.out.println("Кубиков: " + cubeCount(side));
    }

    static long cubeCount(int side) {
        long s = side;
        return s * s * s;
    }
}
