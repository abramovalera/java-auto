import java.util.Scanner;

public class WaterTapSession {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] portions = new int[n];
        for (int i = 0; i < n; i++) {
            portions[i] = scanner.nextInt();
        }
        try (WaterTap tap = new WaterTap()) {
            for (int liters : portions) {
                tap.pour(liters);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

class WaterTap implements AutoCloseable {
    private int bucket;

    WaterTap() {
        System.out.println("Кран открыт");
    }

    void pour(int liters) {
        if (liters <= 0) {
            throw new IllegalArgumentException("литров должно быть больше нуля");
        }
        if (bucket + liters > 10) {
            throw new IllegalStateException("ведро переполнится");
        }
        bucket += liters;
        System.out.println("Налито " + liters + " л, в ведре " + bucket + " л");
    }

    @Override
    public void close() {
        System.out.println("Кран закрыт");
    }
}
