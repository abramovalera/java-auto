import java.util.Scanner;

public class PiggyBankTerminal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PiggyBankAccount account = new PiggyBankAccount(scanner.nextInt());
        int count = scanner.nextInt();
        for (int i = 0; i < count; i++) {
            String command = scanner.next();
            int amount = scanner.nextInt();
            try {
                if (command.equals("пополнить")) {
                    account.deposit(amount);
                    System.out.println("Пополнение " + amount + ", баланс " + account.getBalance());
                } else {
                    account.withdraw(amount);
                    System.out.println("Снятие " + amount + ", баланс " + account.getBalance());
                }
            } catch (PiggyInsufficientFundsException e) {
                System.out.println("Отказ: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Отказ: " + e.getMessage());
            }
        }
        System.out.println("Итоговый баланс: " + account.getBalance());
    }
}

class PiggyInsufficientFundsException extends Exception {
    private final int shortfall;

    PiggyInsufficientFundsException(int balance, int amount) {
        super("не хватает " + (amount - balance) + " (баланс " + balance + ", запрошено " + amount + ")");
        this.shortfall = amount - balance;
    }

    int getShortfall() {
        return shortfall;
    }
}

class PiggyBankAccount {
    private int balance;

    PiggyBankAccount(int balance) {
        this.balance = balance;
    }

    int getBalance() {
        return balance;
    }

    void deposit(int amount) {
        checkPositive(amount);
        balance += amount;
    }

    void withdraw(int amount) throws PiggyInsufficientFundsException {
        checkPositive(amount);
        if (amount > balance) {
            throw new PiggyInsufficientFundsException(balance, amount);
        }
        balance -= amount;
    }

    private void checkPositive(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("сумма должна быть больше нуля: " + amount);
        }
    }
}
