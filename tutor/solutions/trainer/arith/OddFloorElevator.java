import java.util.Scanner;

public class OddFloorElevator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int floor = scanner.nextInt();
        boolean stops = floor % 2 != 0;
        System.out.println(stops);
    }
}
