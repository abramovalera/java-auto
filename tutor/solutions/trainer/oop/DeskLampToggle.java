import java.util.Scanner;

public class DeskLampToggle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String commands = scanner.next();
        DeskLamp lamp = new DeskLamp();
        for (int i = 0; i < commands.length(); i++) {
            char c = commands.charAt(i);
            if (c == 'T') {
                lamp.toggle();
            } else if (c == '+') {
                lamp.brighter();
            } else if (c == '-') {
                lamp.dimmer();
            }
        }
        System.out.println(lamp);
    }
}

class DeskLamp {
    private boolean on;
    private int brightness = 3;

    void toggle() {
        on = !on;
    }

    void brighter() {
        if (on && brightness < 5) {
            brightness++;
        }
    }

    void dimmer() {
        if (on && brightness > 1) {
            brightness--;
        }
    }

    @Override
    public String toString() {
        if (on) {
            return "Лампа включена, яркость " + brightness;
        }
        return "Лампа выключена";
    }
}
