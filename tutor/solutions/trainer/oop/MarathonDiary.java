import java.util.Scanner;

public class MarathonDiary {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MarathonRunner runner = new MarathonRunner(scanner.next());
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            runner.addRun(scanner.nextInt());
        }
        System.out.println(runner.getName() + ": пробежек " + runner.getRuns() + ", всего " + runner.getTotalKm() + " км");
    }
}

class MarathonRunner {
    private final String name;
    private int totalKm;
    private int runs;

    MarathonRunner(String name) {
        this.name = name;
    }

    void addRun(int km) {
        if (km > 0) {
            totalKm += km;
            runs++;
        }
    }

    String getName() {
        return name;
    }

    int getTotalKm() {
        return totalKm;
    }

    int getRuns() {
        return runs;
    }
}
