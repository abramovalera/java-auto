import java.util.Scanner;

public class ExamGradeLetter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        System.out.println(grade(a) + " " + grade(b) + " " + grade(c));
    }

    static char grade(int points) {
        if (points >= 90) {
            return 'A';
        } else if (points >= 75) {
            return 'B';
        } else if (points >= 60) {
            return 'C';
        }
        return 'F';
    }
}
