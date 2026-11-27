import java.util.Scanner;

public class CapitalizeEachWord {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        StringBuilder sb = new StringBuilder();
        if (!line.isBlank()) {
            for (String w : line.trim().split("\\s+")) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(w.substring(0, 1).toUpperCase());
                sb.append(w.substring(1).toLowerCase());
            }
        }
        System.out.println(sb);
    }
}
