import java.util.Scanner;

public class TrafficLightAction {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String color = scanner.nextLine();
        switch (color) {
            case "красный" -> System.out.println("Стой");
            case "жёлтый" -> System.out.println("Жди");
            case "зелёный" -> System.out.println("Иди");
            default -> System.out.println("Светофор сломан");
        }
    }
}
