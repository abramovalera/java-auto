import java.util.Scanner;

public class CrossingLightCycle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CrossingLight light = CrossingLight.valueOf(scanner.next().toUpperCase());
        int switches = scanner.nextInt();
        int total = 0;
        for (int i = 0; i <= switches; i++) {
            System.out.println(light + " — " + light.getSeconds() + " с");
            total += light.getSeconds();
            light = light.next();
        }
        System.out.println("Всего: " + total + " с");
    }
}

enum CrossingLight {
    RED(30), GREEN(25), YELLOW(5);

    private final int seconds;

    CrossingLight(int seconds) {
        this.seconds = seconds;
    }

    int getSeconds() {
        return seconds;
    }

    CrossingLight next() {
        return switch (this) {
            case RED -> GREEN;
            case GREEN -> YELLOW;
            case YELLOW -> RED;
        };
    }
}
