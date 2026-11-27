import java.util.Scanner;

public class TravelTimeMinutes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distance = scanner.nextInt();
        int speed = scanner.nextInt();
        int minutes = distance * 60 / speed;
        System.out.println("В пути: " + minutes + " мин");
    }
}
