import java.util.Scanner;

public class DaysToMilliseconds {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int days = scanner.nextInt();
        long ms = days * 24L * 60 * 60 * 1000;
        System.out.println(ms);
    }
}
