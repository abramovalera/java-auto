import java.util.Locale;
import java.util.Scanner;

public class DebugGradeAverage {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        int[] grades = new int[count];
        for (int i = 0; i < count; i++) {
            grades[i] = in.nextInt();
        }

        double avg = average(grades);
        // Locale.US — чтобы дробная часть печаталась через точку в любой стране
        System.out.println("Средний балл: " + String.format(Locale.US, "%.2f", avg));
        System.out.println("Лучшая оценка: " + best(grades));
        if (avg >= 4.5) {
            System.out.println("Так держать, отличник!");
        }
    }

    static double average(int[] grades) {
        int sum = 0;
        for (int g : grades) {
            sum += g;
        }
        return (double) sum / grades.length;
    }

    static int best(int[] grades) {
        int max = grades[0];
        for (int i = 1; i < grades.length; i++) {
            if (grades[i] > max) {
                max = grades[i];
            }
        }
        return max;
    }
}
