import java.util.Scanner;

public class WallClockAdd {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] parts = scanner.next().split(":");
        WallClockTime time = new WallClockTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            time.addMinutes(scanner.nextInt());
            System.out.println(time);
        }
    }
}

class WallClockTime {
    private int hours;
    private int minutes;

    WallClockTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    void addMinutes(int delta) {
        int total = hours * 60 + minutes + delta;
        total = ((total % 1440) + 1440) % 1440;
        hours = total / 60;
        minutes = total % 60;
    }

    @Override
    public String toString() {
        String h = hours < 10 ? "0" + hours : "" + hours;
        String m = minutes < 10 ? "0" + minutes : "" + minutes;
        return h + ":" + m;
    }
}
