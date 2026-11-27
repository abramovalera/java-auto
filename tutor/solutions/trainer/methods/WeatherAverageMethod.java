import java.util.Scanner;

public class WeatherAverageMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        double[] temps = new double[n];
        for (int i = 0; i < n; i++) {
            temps[i] = scanner.nextDouble();
        }
        double avg = average(temps);
        System.out.println("Средняя температура: " + Math.round(avg * 10) / 10.0);
    }

    static double average(double[] values) {
        if (values.length == 0) {
            return 0.0;
        }
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.length;
    }
}
