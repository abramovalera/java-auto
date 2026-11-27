import java.util.Scanner;

public class BirthYearFromAge {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int currentYear = scanner.nextInt();
        int age = scanner.nextInt();
        int birthYear = currentYear - age;
        System.out.println("Год рождения: " + birthYear);
    }
}
