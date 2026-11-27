import java.util.Scanner;

public class CarComposition {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CompCar car = new CompCar();
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            String command = scanner.next();
            if (command.equals("start")) {
                car.start();
            } else if (command.equals("stop")) {
                car.stop();
            } else {
                car.drive(scanner.nextInt());
            }
        }
        System.out.println("Итого пробег: " + car.getMileage());
    }
}

class CompEngine {
    private boolean running;

    void start() {
        if (running) {
            System.out.println("Двигатель уже работает");
        } else {
            running = true;
            System.out.println("Двигатель запущен");
        }
    }

    void stop() {
        if (!running) {
            System.out.println("Двигатель уже остановлен");
        } else {
            running = false;
            System.out.println("Двигатель остановлен");
        }
    }

    boolean isRunning() {
        return running;
    }
}

class CompCar {
    private final CompEngine engine = new CompEngine();
    private int mileage;

    void start() {
        engine.start();
    }

    void stop() {
        engine.stop();
    }

    void drive(int km) {
        if (!engine.isRunning()) {
            System.out.println("Не едем: двигатель не запущен");
        } else if (km <= 0) {
            System.out.println("Не едем: неверное расстояние");
        } else {
            mileage += km;
            System.out.println("Проехали " + km + " км, пробег " + mileage);
        }
    }

    int getMileage() {
        return mileage;
    }
}
