import java.util.Scanner;

public class WeeklyWagePay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int hours = scanner.nextInt();
        int rate = scanner.nextInt();
        int pay;
        if (hours <= 40) {
            pay = hours * rate;
        } else {
            pay = 40 * rate + (hours - 40) * rate * 2;
        }
        System.out.println("Зарплата: " + pay + " ₽");
    }
}
