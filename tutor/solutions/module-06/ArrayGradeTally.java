import java.util.Scanner;

public class ArrayGradeTally {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] count = new int[6];            // индексы 0…5: ячейка на каждую оценку, count[0] не используем
        for (int i = 0; i < n; i++) {
            int grade = scanner.nextInt();
            count[grade]++;                  // оценка сама становится индексом
        }
        for (int g = 1; g <= 5; g++) {
            System.out.println("Оценка " + g + ": " + count[g]);
        }
    }
}
