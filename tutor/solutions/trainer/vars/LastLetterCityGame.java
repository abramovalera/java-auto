import java.util.Scanner;

public class LastLetterCityGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String city = scanner.nextLine();
        char last = city.charAt(city.length() - 1);
        System.out.println("Следующий город на букву: " + last);
    }
}
