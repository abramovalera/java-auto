import java.util.Scanner;

public class GuardedAccountDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GuardedAccount account = new GuardedAccount();
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            String op = scanner.next();
            int amount = scanner.nextInt();
            if (op.equals("+")) {
                if (account.deposit(amount)) {
                    System.out.println("Положили " + amount + ", баланс " + account.getBalance());
                } else {
                    System.out.println("Отказ: положить " + amount + ", баланс " + account.getBalance());
                }
            } else {
                if (account.withdraw(amount)) {
                    System.out.println("Сняли " + amount + ", баланс " + account.getBalance());
                } else {
                    System.out.println("Отказ: снять " + amount + ", баланс " + account.getBalance());
                }
            }
        }
        System.out.println("Итоговый баланс: " + account.getBalance());
    }
}

class GuardedAccount {
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
