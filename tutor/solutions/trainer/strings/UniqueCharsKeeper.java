import java.util.Scanner;

public class UniqueCharsKeeper {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String c = String.valueOf(text.charAt(i));
            if (sb.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        System.out.println(sb.toString());
    }
}
