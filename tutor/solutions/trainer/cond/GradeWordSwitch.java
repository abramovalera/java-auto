import java.util.Scanner;

public class GradeWordSwitch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int grade = scanner.nextInt();
        switch (grade) {
            case 5 -> System.out.println("отлично");
            case 4 -> System.out.println("хорошо");
            case 3 -> System.out.println("удовлетворительно");
            case 2 -> System.out.println("неудовлетворительно");
            default -> System.out.println("Нет такой оценки");
        }
    }
}
