import java.util.Scanner;

public class FamilyTransfer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FamilyAccount first = new FamilyAccount(scanner.next(), scanner.nextInt());
        FamilyAccount second = new FamilyAccount(scanner.next(), scanner.nextInt());
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            String from = scanner.next();
            int amount = scanner.nextInt();
            FamilyAccount sender = from.equals(first.getOwner()) ? first : second;
            FamilyAccount receiver = sender == first ? second : first;
            if (sender.transferTo(receiver, amount)) {
                System.out.println(sender.getOwner() + " -> " + receiver.getOwner() + ": " + amount);
            } else {
                System.out.println("Отказ: " + sender.getOwner() + " не может перевести " + amount);
            }
        }
        System.out.println(first);
        System.out.println(second);
    }
}

class FamilyAccount {
    private final String owner;
    private int balance;

    FamilyAccount(String owner, int balance) {
        this.owner = owner;
        this.balance = Math.max(0, balance);
    }

    boolean transferTo(FamilyAccount other, int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        other.balance += amount;
        return true;
    }

    String getOwner() {
        return owner;
    }

    int getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return owner + ": " + balance + " руб.";
    }
}
