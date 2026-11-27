import java.util.Scanner;

public class MobilePlanBill {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int minutes = scanner.nextInt();
        int total = 500;
        if (minutes > 300) {
            total = total + (minutes - 300) * 2;
        }
        System.out.println("К оплате: " + total + " ₽");
    }
}
