import java.util.Scanner;

public class SmartHomeHub {
    public static void main(String[] args) {
        HomeDevice[] devices = { new HomeLamp(), new HomeHeater(), new HomeTv() };
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            String command = in.next();
            String target = in.next();
            boolean found = false;
            for (HomeDevice d : devices) {
                if (target.equals("all") || d.id().equals(target)) {
                    found = true;
                    if (command.equals("on")) {
                        d.turnOn();
                    } else {
                        d.turnOff();
                    }
                }
            }
            if (!found) {
                System.out.println("Нет устройства: " + target);
            }
        }
        int power = 0;
        for (HomeDevice d : devices) {
            System.out.println(d.status());
            power += d.power();
        }
        System.out.println("Потребление: " + power + " Вт");
    }
}

interface HomeDevice {
    String id();
    String title();
    void turnOn();
    void turnOff();
    boolean isOn();
    int power();

    default String status() {
        return title() + ": " + (isOn() ? "вкл" : "выкл");
    }
}

abstract class HomeBaseDevice implements HomeDevice {
    private boolean on;

    public void turnOn() { on = true; }
    public void turnOff() { on = false; }
    public boolean isOn() { return on; }
}

class HomeLamp extends HomeBaseDevice {
    public String id() { return "lamp"; }
    public String title() { return "Лампа"; }
    public int power() { return isOn() ? 60 : 0; }
}

class HomeHeater extends HomeBaseDevice {
    public String id() { return "heater"; }
    public String title() { return "Обогреватель"; }
    public int power() { return isOn() ? 1500 : 0; }
}

class HomeTv extends HomeBaseDevice {
    public String id() { return "tv"; }
    public String title() { return "Телевизор"; }
    public int power() { return isOn() ? 120 : 1; } // выключенный телевизор ждёт пульт и ест 1 Вт
}
