import java.util.Scanner;

public class AgeValidationGate {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine().trim();
            try {
                int age = validateAge(Integer.parseInt(line));
                System.out.println("Возраст принят: " + age);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: «" + line + "» — не число");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    static int validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("возраст не может быть отрицательным: " + age);
        }
        if (age > 150) {
            throw new IllegalArgumentException("возраст больше 150: " + age);
        }
        return age;
    }
}
