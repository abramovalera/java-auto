import java.util.Scanner;

public class StreetThermometerDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double celsius = scanner.nextDouble();
        StreetThermometer thermometer = new StreetThermometer(celsius);
        double fahrenheit = thermometer.toFahrenheit();
        System.out.println("По Фаренгейту: " + Math.round(fahrenheit * 10) / 10.0);
        System.out.println("Мороз: " + thermometer.isFrost());
    }
}

class StreetThermometer {
    double celsius;

    StreetThermometer(double celsius) {
        this.celsius = celsius;
    }

    double toFahrenheit() {
        return celsius * 9 / 5 + 32;
    }

    boolean isFrost() {
        return celsius < 0;
    }
}
