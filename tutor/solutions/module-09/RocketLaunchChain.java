import java.util.Scanner;

public class RocketLaunchChain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String mission = sc.nextLine();
        int pressure = sc.nextInt();

        RocketControlPanel panel = new RocketControlPanel();
        panel.launch(mission, pressure);
    }
}

class RocketControlPanel {
    private final RocketFlightComputer computer = new RocketFlightComputer();

    void launch(String mission, int pressure) {
        System.out.println("Пульт: миссия «" + mission + "», начинаю отсчёт");
        boolean ok = computer.prepare(pressure);
        if (ok) {
            System.out.println("Пульт: ПОЕХАЛИ!");
        } else {
            System.out.println("Пульт: старт отменён");
        }
    }
}

class RocketFlightComputer {
    private final RocketFuelSystem fuel = new RocketFuelSystem();

    boolean prepare(int pressure) {
        System.out.println("  Компьютер: проверка систем");
        boolean ok = fuel.fill(pressure);
        System.out.println("  Компьютер: проверка завершена");
        return ok;
    }
}

class RocketFuelSystem {
    private final RocketFuelTank tank = new RocketFuelTank();

    boolean fill(int pressure) {
        System.out.println("    Топливо: заправка");
        boolean ok = tank.drain(pressure);
        System.out.println("    Топливо: готово");
        return ok;
    }
}

class RocketFuelTank {
    private final RocketFuelPump pump = new RocketFuelPump();

    boolean drain(int pressure) {
        System.out.println("      Бак: открываю");
        boolean ok = pump.pump(pressure);
        System.out.println("      Бак: закрываю");
        return ok;
    }
}

class RocketFuelPump {
    private final RocketValve valve = new RocketValve();

    boolean pump(int pressure) {
        System.out.println("        Насос: подача топлива");
        boolean ok = valve.open(pressure);
        System.out.println("        Насос: стоп");
        return ok;
    }
}

class RocketValve {
    private final RocketEngine engine = new RocketEngine();

    boolean open(int pressure) {
        System.out.println("          Клапан: открыт");
        boolean ok = engine.start(pressure);
        System.out.println("          Клапан: закрыт");
        return ok;
    }
}

class RocketEngine {
    private final RocketIgniter igniter = new RocketIgniter();

    boolean start(int pressure) {
        System.out.println("            Двигатель: подготовка");
        boolean ok = igniter.spark(pressure);
        if (ok) {
            System.out.println("            Двигатель: тяга есть");
        } else {
            System.out.println("            Двигатель: нет зажигания");
        }
        return ok;
    }
}

class RocketIgniter {
    private final RocketPressureSensor sensor = new RocketPressureSensor();

    boolean spark(int pressure) {
        System.out.println("              Запал: проверяю давление");
        int measured = sensor.measure(pressure);
        boolean ok = measured >= 100;
        if (ok) {
            System.out.println("              Запал: искра!");
        } else {
            System.out.println("              Запал: мало давления");
        }
        return ok;
    }
}

class RocketPressureSensor {
    int measure(int pressure) {
        System.out.println("                Датчик: давление " + pressure);
        return pressure;
    }
}
