import java.util.Scanner;

public class YearsUntilDriving {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int age = scanner.nextInt();
        int years = Math.max(0, 18 - age);
        System.out.println("Лет до прав: " + years);
    }
}
