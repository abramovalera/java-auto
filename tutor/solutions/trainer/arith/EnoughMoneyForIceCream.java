import java.util.Scanner;

public class EnoughMoneyForIceCream {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int money = scanner.nextInt();
        int price = scanner.nextInt();
        int count = scanner.nextInt();
        boolean enough = money >= price * count;
        System.out.println(enough);
    }
}
