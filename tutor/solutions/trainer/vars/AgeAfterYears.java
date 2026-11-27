import java.util.Scanner;

public class AgeAfterYears {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Сколько тебе лет?");
        int age = scanner.nextInt();
        System.out.println("Через сколько лет?");
        int years = scanner.nextInt();
        System.out.println("Тогда тебе будет " + (age + years));
    }
}
