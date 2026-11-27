import java.util.Scanner;

public class ClickCounterDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String presses = scanner.next();
        TallyCounter counter = new TallyCounter();
        for (int i = 0; i < presses.length(); i++) {
            if (presses.charAt(i) == '+') {
                counter.click();
            } else {
                counter.reset();
            }
        }
        System.out.println("На счётчике: " + counter.getCount());
        System.out.println("Всего нажатий: " + counter.getTotal());
    }
}

class TallyCounter {
    private int count;
    private int total;

    void click() {
        count++;
        total++;
    }

    void reset() {
        count = 0;
    }

    int getCount() {
        return count;
    }

    int getTotal() {
        return total;
    }
}
