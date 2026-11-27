import java.util.Scanner;

public class GameCommandSwitch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        switch (command.toLowerCase()) {
            case "start" -> System.out.println("Игра началась");
            case "pause" -> System.out.println("Пауза");
            case "exit" -> System.out.println("До встречи!");
            default -> System.out.println("Неизвестная команда: " + command);
        }
    }
}
