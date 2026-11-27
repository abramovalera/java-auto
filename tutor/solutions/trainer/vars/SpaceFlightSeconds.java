import java.util.Scanner;

public class SpaceFlightSeconds {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int days = scanner.nextInt();
        long seconds = days * 24L * 60 * 60;
        System.out.println("Полёт длится " + seconds + " секунд");
    }
}
