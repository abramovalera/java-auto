import java.util.Scanner;

public class VowelCountInText {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        String vowels = "аеёиоуыэюяaeiou";
        int count = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if (vowels.indexOf(c) != -1) {
                count++;
            }
        }
        System.out.println(count);
    }
}
