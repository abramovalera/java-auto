import java.util.Scanner;

public class WaterSlideAdmission {
    static void checkHeight(int height) throws WaterSlideHeightException {
        if (height < 120) {
            throw new WaterSlideHeightException("нужно подрасти на " + (120 - height) + " см");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int height = scanner.nextInt();
            try {
                checkHeight(height);
                System.out.println("Проходи!");
            } catch (WaterSlideHeightException e) {
                System.out.println("Стоп: " + e.getMessage());
            }
        }
    }
}

class WaterSlideHeightException extends Exception {
    WaterSlideHeightException(String message) {
        super(message);
    }
}
