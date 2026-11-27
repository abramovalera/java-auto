import java.util.Scanner;

public class DropFractionPart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double temperature = scanner.nextDouble();
        int whole = (int) temperature;
        System.out.println("Целых градусов: " + whole);
    }
}
