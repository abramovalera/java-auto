import java.util.Scanner;

// Температуры за N дней: самая низкая, самая высокая и сколько дней был мороз (ниже нуля).
// Программа компилируется, но в ней три ошибки. Найди и исправь их, не переписывая заново.
public class FixArrayTemperatures {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] temps = new int[n];
        for (int i = 0; i < n; i++) {
            temps[i] = scanner.nextInt();
        }

        int min = 0;
        int max = temps[0];
        int frostDays = 0;
        for (int i = 0; i <= temps.length; i++) {
            if (temps[i] < min) {
                min = temps[i];
            }
            if (temps[i] > max) {
                max = temps[i];
            }
            if (temps[i] <= 0) {
                frostDays++;
            }
        }
        System.out.println("Самая низкая: " + min);
        System.out.println("Самая высокая: " + max);
        System.out.println("Дней с морозом: " + frostDays);
    }
}
