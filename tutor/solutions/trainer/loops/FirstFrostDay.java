import java.util.Scanner;

public class FirstFrostDay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int frostDay = 0;
        for (int day = 1; day <= n; day++) {
            int t = scanner.nextInt();
            if (t < 0) {
                frostDay = day;
                break;
            }
        }
        if (frostDay == 0) {
            System.out.println("Морозов не было");
        } else {
            System.out.println("Первый мороз: день " + frostDay);
        }
    }
}
