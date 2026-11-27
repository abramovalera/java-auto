import java.util.Scanner;

public class RaceTimeSeconds {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int h = scanner.nextInt();
        int m = scanner.nextInt();
        int s = scanner.nextInt();
        System.out.println("Время в секундах: " + toSeconds(h, m, s));
    }

    static int toSeconds(int hours, int minutes, int seconds) {
        return hours * 3600 + minutes * 60 + seconds;
    }
}
