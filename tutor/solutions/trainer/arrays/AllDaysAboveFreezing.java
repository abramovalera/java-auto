import java.util.Scanner;

public class AllDaysAboveFreezing {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] temps = new int[n];
        for (int i = 0; i < n; i++) {
            temps[i] = scanner.nextInt();
        }
        int frostDay = -1;
        for (int i = 0; i < n; i++) {
            if (temps[i] <= 0) {
                frostDay = i + 1;
                break;
            }
        }
        if (frostDay == -1) {
            System.out.println("Без заморозков");
        } else {
            System.out.println("Первый заморозок: день " + frostDay);
        }
    }
}
