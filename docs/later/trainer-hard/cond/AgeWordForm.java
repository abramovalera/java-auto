import java.util.Scanner;

public class AgeWordForm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int age = scanner.nextInt();
        int lastTwo = age % 100;
        int last = age % 10;
        String word;
        if (lastTwo >= 11 && lastTwo <= 14) {
            word = "лет";
        } else if (last == 1) {
            word = "год";
        } else if (last >= 2 && last <= 4) {
            word = "года";
        } else {
            word = "лет";
        }
        System.out.println(age + " " + word);
    }
}
