// Эталон задачи m02-t05. celsius * 9 считается в double, поэтому деление на 5 уже дробное.
// Ловушка: 9 / 5 * celsius даст 1 * celsius (целочисленное деление).
import java.util.Scanner;

public class CelsiusToFahrenheit {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Температура в °C:");
        double celsius = scanner.nextDouble();
        double fahrenheit = celsius * 9 / 5 + 32;
        System.out.println(celsius + " °C = " + fahrenheit + " °F");
    }
}
