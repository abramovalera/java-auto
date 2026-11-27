import java.util.Scanner;

public class TrainTripInfo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String from = scanner.next();
        String to = scanner.next();
        int minutes = scanner.nextInt();
        TrainTrip trip = new TrainTrip(from, to, minutes);
        System.out.println(trip.route());
        System.out.println("В пути: " + trip.duration());
    }
}

class TrainTrip {
    String from;
    String to;
    int minutes;

    TrainTrip(String from, String to, int minutes) {
        this.from = from;
        this.to = to;
        this.minutes = minutes;
    }

    String route() {
        return from + " -> " + to;
    }

    String duration() {
        int h = minutes / 60;
        int m = minutes % 60;
        if (h == 0) {
            return m + " мин";
        }
        if (m == 0) {
            return h + " ч";
        }
        return h + " ч " + m + " мин";
    }
}
