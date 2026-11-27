import java.util.Scanner;

public class UniversityAdmission {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int sum = a + b + c;
        if (a < 40 || b < 40 || c < 40) {
            System.out.println("Не набран минимум по экзамену");
        } else if (sum < 220) {
            System.out.println("Не хватило баллов: " + (220 - sum));
        } else {
            System.out.println("Поступил! Сумма: " + sum);
        }
    }
}
