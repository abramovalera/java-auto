import java.util.Scanner;

public class CardWithdrawalLog {
    static int balance;

    static void withdraw(int amount) throws CardBalanceTooLowException {
        if (amount <= 0) {
            throw new IllegalArgumentException("сумма должна быть больше нуля");
        }
        if (amount > balance) {
            throw new CardBalanceTooLowException("не хватает " + (amount - balance) + " руб.");
        }
        balance -= amount;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        balance = scanner.nextInt();
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int amount = scanner.nextInt();
            try {
                withdraw(amount);
                System.out.println("Снято " + amount + ", осталось " + balance);
            } catch (CardBalanceTooLowException | IllegalArgumentException e) {
                System.out.println("Отказ: " + e.getMessage());
            }
        }
        System.out.println("Итоговый баланс: " + balance);
    }
}

class CardBalanceTooLowException extends Exception {
    CardBalanceTooLowException(String message) {
        super(message);
    }
}
