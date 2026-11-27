import java.util.Scanner;

public class GreetingByHour {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int hour = scanner.nextInt();
        if (hour < 0 || hour > 23) {
            System.out.println("Нет такого часа");
        } else if (hour >= 5 && hour <= 11) {
            System.out.println("Доброе утро");
        } else if (hour >= 12 && hour <= 17) {
            System.out.println("Добрый день");
        } else if (hour >= 18 && hour <= 22) {
            System.out.println("Добрый вечер");
        } else {
            System.out.println("Доброй ночи");
        }
    }
}
