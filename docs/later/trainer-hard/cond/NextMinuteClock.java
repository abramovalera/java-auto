import java.util.Scanner;

public class NextMinuteClock {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int h = scanner.nextInt();
        int m = scanner.nextInt();
        if (h < 0 || h > 23 || m < 0 || m > 59) {
            System.out.println("Неверное время");
        } else {
            m++;
            if (m == 60) {
                m = 0;
                h++;
                if (h == 24) {
                    h = 0;
                }
            }
            String hours = (h < 10 ? "0" : "") + h;
            String minutes = (m < 10 ? "0" : "") + m;
            System.out.println(hours + ":" + minutes);
        }
    }
}
