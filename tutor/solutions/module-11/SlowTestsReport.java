import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class SlowTestsReport {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        TimedTest[] tests = new TimedTest[n];
        for (int i = 0; i < n; i++) {
            tests[i] = new TimedTest(in.next(), in.nextInt());
        }
        Arrays.sort(tests, new TimedTestComparator());
        for (int i = 0; i < n; i++) {
            System.out.println((i + 1) + ". " + tests[i].name + " — " + tests[i].millis + " мс");
        }
    }
}

class TimedTest {
    final String name;
    final int millis;

    TimedTest(String name, int millis) {
        this.name = name;
        this.millis = millis;
    }
}

class TimedTestComparator implements Comparator<TimedTest> {
    @Override
    public int compare(TimedTest a, TimedTest b) {
        if (a.millis != b.millis) {
            return Integer.compare(b.millis, a.millis); // сначала самые долгие
        }
        return a.name.compareTo(b.name);                // при равенстве — по алфавиту
    }
}
