import java.util.Scanner;

public class CardTerminalApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DebitCard card = new DebitCard();
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String op = sc.next();
            int amount = sc.nextInt();
            if (amount <= 0) {
                System.out.println("Ошибка: сумма должна быть больше нуля");
            } else if (op.equals("+")) {
                card.deposit(amount);
                System.out.println("Пополнение " + amount + ": баланс " + card.getBalance());
            } else if (card.withdraw(amount)) {
                System.out.println("Снятие " + amount + ": баланс " + card.getBalance());
            } else {
                System.out.println("Отказ: недостаточно средств, баланс " + card.getBalance());
            }
        }
        System.out.println("Итоговый баланс: " + card.getBalance());
    }
}

class DebitCard {
    private int balance;

    boolean deposit(int amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    boolean withdraw(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    int getBalance() {
        return balance;
    }
}
