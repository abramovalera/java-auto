import java.util.Scanner;

public class AtmBanknoteSplit {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int amount = scanner.nextInt();
        int n5000 = amount / 5000;
        amount %= 5000;
        int n1000 = amount / 1000;
        amount %= 1000;
        int n500 = amount / 500;
        amount %= 500;
        int n100 = amount / 100;
        System.out.println("5000: " + n5000 + ", 1000: " + n1000 + ", 500: " + n500 + ", 100: " + n100);
    }
}
