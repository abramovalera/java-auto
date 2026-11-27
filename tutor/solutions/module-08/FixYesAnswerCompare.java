import java.util.Scanner;

public class FixYesAnswerCompare {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        answer = answer.trim();
        answer = answer.toLowerCase();
        if (answer.equals("да")) {
            System.out.println("Продолжаем!");
        } else {
            System.out.println("Стоп.");
        }
    }
}
