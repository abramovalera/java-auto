import java.util.Scanner;

public class RockPaperScissorsRound {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String first = scanner.nextLine();
        String second = scanner.nextLine();
        boolean firstValid = first.equals("камень") || first.equals("ножницы") || first.equals("бумага");
        boolean secondValid = second.equals("камень") || second.equals("ножницы") || second.equals("бумага");
        if (!firstValid || !secondValid) {
            System.out.println("Неверный ход");
        } else if (first.equals(second)) {
            System.out.println("Ничья");
        } else if (first.equals("камень") && second.equals("ножницы")
                || first.equals("ножницы") && second.equals("бумага")
                || first.equals("бумага") && second.equals("камень")) {
            System.out.println("Победил первый игрок");
        } else {
            System.out.println("Победил второй игрок");
        }
    }
}
