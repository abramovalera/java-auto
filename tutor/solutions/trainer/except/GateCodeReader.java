import java.util.Scanner;

public class GateCodeReader {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            try {
                int code = Integer.parseInt(line);
                if (code == 2580) {
                    System.out.println("Ворота открыты");
                } else {
                    System.out.println("Неверный код");
                }
            } catch (NumberFormatException e) {
                System.out.println("Код набирают только цифрами");
            }
        }
    }
}
