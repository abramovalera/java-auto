import java.util.Scanner;

public class PhoneBatteryLevel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PhoneBattery battery = new PhoneBattery(scanner.nextInt());
        System.out.println("Заряд: " + battery.getCharge() + "%");
        battery.charge(scanner.nextInt());
        System.out.println("После зарядки: " + battery.getCharge() + "%");
        if (battery.use(scanner.nextInt())) {
            System.out.println("После игры: " + battery.getCharge() + "%");
        } else {
            System.out.println("Телефон сел");
        }
    }
}

class PhoneBattery {
    private int charge;

    PhoneBattery(int charge) {
        if (charge < 0) {
            charge = 0;
        }
        if (charge > 100) {
            charge = 100;
        }
        this.charge = charge;
    }

    void charge(int percent) {
        charge = Math.min(100, charge + percent);
    }

    boolean use(int percent) {
        if (percent > charge) {
            charge = 0;
            return false;
        }
        charge -= percent;
        return true;
    }

    int getCharge() {
        return charge;
    }
}
