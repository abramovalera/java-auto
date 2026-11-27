import java.util.Scanner;

public class DachaPlotFence {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int length = scanner.nextInt();
        int width = scanner.nextInt();
        int fence = (length + width) * 2;
        int area = length * width;
        System.out.println("Забор: " + fence + " м");
        System.out.println("Площадь: " + area + " кв. м");
    }
}
