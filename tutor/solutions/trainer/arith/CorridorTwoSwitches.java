import java.util.Scanner;

public class CorridorTwoSwitches {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean first = scanner.nextBoolean();
        boolean second = scanner.nextBoolean();
        boolean lightOn = first != second;
        System.out.println(lightOn);
    }
}
