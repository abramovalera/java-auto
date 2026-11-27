import java.util.Scanner;

public class RoomFloorArea {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double length = scanner.nextDouble();
        double width = scanner.nextDouble();
        double area = length * width;
        System.out.println("Площадь комнаты: " + area + " кв. м");
    }
}
