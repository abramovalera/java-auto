import java.util.Scanner;

public class CinemaAgeValidator {
    static void checkAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("возраст не может быть отрицательным");
        }
        if (age > 120) {
            throw new IllegalArgumentException("столько не живут");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int age = scanner.nextInt();
        try {
            checkAge(age);
            System.out.println("Возраст принят: " + age);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
