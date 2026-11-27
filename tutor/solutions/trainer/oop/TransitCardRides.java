import java.util.Scanner;

public class TransitCardRides {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransitCard card = new TransitCard(scanner.nextInt());
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int fare = scanner.nextInt();
            if (card.pay(fare)) {
                System.out.println("Проход, остаток " + card.getBalance());
            } else {
                System.out.println("Отказ, остаток " + card.getBalance());
            }
        }
    }
}

class TransitCard {
    private int balance;

    TransitCard(int balance) {
        if (balance < 0) {
            balance = 0;
        }
        this.balance = balance;
    }

    boolean pay(int fare) {
        if (fare > balance) {
            return false;
        }
        balance -= fare;
        return true;
    }

    int getBalance() {
        return balance;
    }
}
