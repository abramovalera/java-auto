import java.util.Scanner;

public class CinemaSeatLabel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CinemaSeat first = new CinemaSeat(scanner.nextInt(), scanner.nextInt());
        CinemaSeat second = new CinemaSeat(scanner.nextInt(), scanner.nextInt());
        System.out.println(first.label() + (first.isVip() ? " (VIP)" : ""));
        System.out.println(second.label() + (second.isVip() ? " (VIP)" : ""));
        System.out.println("Одно и то же место: " + first.equals(second));
    }
}

record CinemaSeat(int row, int place) {
    String label() {
        return "Ряд " + row + ", место " + place;
    }

    boolean isVip() {
        return row >= 5 && row <= 8;
    }
}
