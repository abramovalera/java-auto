import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class BugQueue {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        BugTicket[] bugs = new BugTicket[n];
        for (int i = 0; i < n; i++) {
            int id = in.nextInt();
            BugSeverity severity = BugSeverity.valueOf(in.next());
            String title = in.next();
            bugs[i] = new BugTicket(id, severity, title);
        }
        Arrays.sort(bugs, new Comparator<BugTicket>() {
            @Override
            public int compare(BugTicket a, BugTicket b) {
                int bySeverity = a.severity().compareTo(b.severity()); // enum сравнивается по порядку объявления
                if (bySeverity != 0) {
                    return bySeverity;
                }
                return Integer.compare(a.id(), b.id());
            }
        });
        int hours = 0;
        for (BugTicket bug : bugs) {
            System.out.println("#" + bug.id() + " [" + bug.severity() + "] " + bug.title());
            hours += bug.severity().getHours();
        }
        System.out.println("Часов на всё: " + hours);
    }
}

enum BugSeverity {
    BLOCKER(8), CRITICAL(4), MAJOR(2), MINOR(1);

    private final int hours;

    BugSeverity(int hours) {
        this.hours = hours;
    }

    int getHours() {
        return hours;
    }
}

record BugTicket(int id, BugSeverity severity, String title) {
}
