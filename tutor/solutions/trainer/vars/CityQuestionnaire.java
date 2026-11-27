import java.util.Scanner;

public class CityQuestionnaire {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int age = scanner.nextInt();
        scanner.nextLine();
        String city = scanner.nextLine();
        System.out.println("Тебе " + age + ", ты из города " + city);
    }
}
