import java.util.Scanner;

public class ServerPortConfig {
    static int readPort(String text) {
        int port;
        try {
            port = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Порт задан неверно: " + text, e);
        }
        if (port < 1 || port > 65535) {
            throw new IllegalStateException("Порт вне диапазона: " + port);
        }
        return port;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            int port = readPort(line);
            System.out.println("Сервер слушает порт " + port);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Причина: " + e.getCause().getClass().getSimpleName());
            }
        }
    }
}
