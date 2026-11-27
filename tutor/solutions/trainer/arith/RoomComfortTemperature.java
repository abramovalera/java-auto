import java.util.Scanner;

public class RoomComfortTemperature {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        boolean comfortable = t >= 18 && t <= 24;
        System.out.println(comfortable);
    }
}
