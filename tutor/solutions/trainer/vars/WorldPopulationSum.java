import java.util.Scanner;

public class WorldPopulationSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long first = scanner.nextLong();
        long second = scanner.nextLong();
        long total = first + second;
        System.out.println("Вместе: " + total + " человек");
    }
}
