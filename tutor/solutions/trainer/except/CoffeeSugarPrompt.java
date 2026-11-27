import java.util.Scanner;

public class CoffeeSugarPrompt {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        while (true) {
            String line = scanner.nextLine();
            attempts++;
            try {
                int spoons = Integer.parseInt(line);
                if (spoons < 0 || spoons > 5) {
                    System.out.println("Можно от 0 до 5");
                    continue;
                }
                System.out.println("Готовлю кофе, ложек сахара: " + spoons);
                System.out.println("Попыток: " + attempts);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Нужна цифра");
            }
        }
    }
}
