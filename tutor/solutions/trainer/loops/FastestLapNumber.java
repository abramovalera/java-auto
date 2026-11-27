import java.util.Scanner;

public class FastestLapNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int bestTime = scanner.nextInt();
        int bestLap = 1;
        for (int lap = 2; lap <= n; lap++) {
            int time = scanner.nextInt();
            if (time < bestTime) {
                bestTime = time;
                bestLap = lap;
            }
        }
        System.out.println("Лучший круг: " + bestLap + ", время: " + bestTime);
    }
}
