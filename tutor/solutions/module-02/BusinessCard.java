// Эталон задачи m02-t09. Ловушка: после nextInt() нужен лишний nextLine(),
// иначе город прочитается как пустая строка.
// Альтернатива: int age = Integer.parseInt(scanner.nextLine());
import java.util.Scanner;

public class BusinessCard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Имя:");
        String name = scanner.nextLine();
        System.out.println("Возраст:");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Город:");
        String city = scanner.nextLine();

        System.out.println("=== ВИЗИТКА ===");
        System.out.println("Имя: " + name);
        System.out.println("Возраст: " + age);
        System.out.println("Город: " + city);
        System.out.println("Через 10 лет: " + (age + 10));
    }
}
