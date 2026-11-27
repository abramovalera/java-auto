import java.util.Scanner;

public class NextCouponNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine();
        int number = Integer.parseInt(code);
        System.out.println("Заказ №" + code + " принят. Следующий номер: " + (number + 1));
    }
}
