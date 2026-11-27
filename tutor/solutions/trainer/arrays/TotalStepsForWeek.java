import java.util.Scanner;

public class TotalStepsForWeek {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] steps = new int[n];
        for (int i = 0; i < n; i++) {
            steps[i] = scanner.nextInt();
        }
        int total = 0;
        for (int s : steps) {
            total += s;
        }
        System.out.println("Всего шагов: " + total);
    }
}
