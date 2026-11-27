import java.util.Scanner;

public class FahrenheitWeatherMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double celsius = scanner.nextDouble();
        System.out.println(celsius + " °C = " + toFahrenheit(celsius) + " °F");
    }

    static double toFahrenheit(double celsius) {
        return celsius * 9 / 5 + 32;
    }
}
