import java.util.Scanner;

public class ChineseZodiacYear {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        String animal = switch (year % 12) {
            case 0 -> "Обезьяна";
            case 1 -> "Петух";
            case 2 -> "Собака";
            case 3 -> "Свинья";
            case 4 -> "Крыса";
            case 5 -> "Бык";
            case 6 -> "Тигр";
            case 7 -> "Кролик";
            case 8 -> "Дракон";
            case 9 -> "Змея";
            case 10 -> "Лошадь";
            default -> "Коза";
        };
        System.out.println("Год: " + animal);
    }
}
