import java.util.Scanner;

public class RobotVacuumRoute {
    static int x = 0;
    static int y = 0;

    static void execute(String command) throws RobotVacuumWallException {
        String[] parts = command.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("нужны команда и число шагов");
        }
        int steps = Integer.parseInt(parts[1]);
        if (steps <= 0) {
            throw new IllegalArgumentException("шагов должно быть больше нуля");
        }
        int newX = x;
        int newY = y;
        switch (parts[0]) {
            case "up":
                newY += steps;
                break;
            case "down":
                newY -= steps;
                break;
            case "right":
                newX += steps;
                break;
            case "left":
                newX -= steps;
                break;
            default:
                throw new IllegalArgumentException("неизвестная команда " + parts[0]);
        }
        if (newX < 0 || newX > 9 || newY < 0 || newY > 9) {
            throw new RobotVacuumWallException("впереди стена, робот стоит на месте");
        }
        x = newX;
        y = newY;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        int errors = 0;
        for (int i = 1; i <= n; i++) {
            String command = scanner.nextLine();
            try {
                execute(command);
            } catch (NumberFormatException e) {
                System.out.println("Строка " + i + ": шаги — это целое число");
                errors++;
            } catch (IllegalArgumentException | RobotVacuumWallException e) {
                System.out.println("Строка " + i + ": " + e.getMessage());
                errors++;
            }
        }
        System.out.println("Робот в точке (" + x + ", " + y + ")");
        System.out.println("Ошибок: " + errors);
    }
}

class RobotVacuumWallException extends Exception {
    RobotVacuumWallException(String message) {
        super(message);
    }
}
