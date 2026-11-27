import java.util.Scanner;

public class TvDiagonalInCm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double inches = scanner.nextDouble();
        int centimeters = (int) (inches * 2.54);
        System.out.println("Диагональ: примерно " + centimeters + " см");
    }
}
