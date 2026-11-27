import java.util.Scanner;

public class DigitalClockFormat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int h = scanner.nextInt();
        int m = scanner.nextInt();
        String hours = (h < 10 ? "0" : "") + h;
        String minutes = (m < 10 ? "0" : "") + m;
        System.out.println(hours + ":" + minutes);
    }
}
