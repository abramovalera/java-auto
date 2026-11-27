import java.util.Scanner;

// Касса кинотеатра. Программа компилируется, но печатает неправильные билеты.
// Найди и исправь три ошибки, не переписывая программу заново.
public class FixTicketPrice {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int age = scanner.nextInt();

        if (age < 0) {
            System.out.println("Ошибка: неверный возраст");
        } else if (age >= 18) {
            System.out.println("Взрослый билет: 300 руб.");
        } else if (age >= 65) {
            System.out.println("Пенсионный билет: 100 руб.");
        } else if (age >= 7) {
            System.out.println("Детский билет: 150 руб.");
        } else
            System.out.println("Бесплатно");
            System.out.println("Только вместе со взрослым");

        if (age >= 65); {
            System.out.println("Покажите пенсионное удостоверение");
        }
    }
}
