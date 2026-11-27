import java.util.Scanner;

public class TextCharStats {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        String vowelLetters = "аеёиоуыэюяaeiou";
        int vowels = 0;
        int consonants = 0;
        int digits = 0;
        int others = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = Character.toLowerCase(text.charAt(i));
            if (Character.isLetter(c)) {
                if (vowelLetters.indexOf(c) >= 0) {
                    vowels++;
                } else {
                    consonants++;
                }
            } else if (Character.isDigit(c)) {
                digits++;
            } else {
                others++;
            }
        }
        System.out.println("гласных: " + vowels);
        System.out.println("согласных: " + consonants);
        System.out.println("цифр: " + digits);
        System.out.println("прочих: " + others);
    }
}
