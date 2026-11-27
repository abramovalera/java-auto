import java.util.Scanner;

public class DiceRollResult {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int first = scanner.nextInt();
        int second = scanner.nextInt();
        if (first == second) {
            System.out.println("Дубль! Ходи ещё раз");
        } else if (first + second == 7) {
            System.out.println("Семёрка! Бери бонус");
        } else {
            System.out.println("Сумма: " + (first + second));
        }
    }
}
