import java.util.Scanner;

public class TestRunSummary {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] counts = new int[RunStatus.values().length];
        for (int i = 0; i < n; i++) {
            in.next(); // имя теста для подсчёта не нужно
            RunStatus status = RunStatus.valueOf(in.next().toUpperCase());
            counts[status.ordinal()]++;
        }
        for (RunStatus s : RunStatus.values()) {
            System.out.println(s + ": " + counts[s.ordinal()]);
        }
        if (counts[RunStatus.FAILED.ordinal()] > 0) {
            System.out.println("Прогон красный");
        } else if (counts[RunStatus.PASSED.ordinal()] > 0) {
            System.out.println("Прогон зелёный");
        } else {
            System.out.println("Тесты не запускались");
        }
    }
}

enum RunStatus {
    PASSED, FAILED, SKIPPED
}
