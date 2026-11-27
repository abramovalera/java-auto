import java.util.Scanner;

public class RunLengthCompress {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            int count = 0;
            while (i < s.length() && s.charAt(i) == c) {
                count++;
                i++;
            }
            sb.append(c).append(count);
        }
        System.out.println(sb);
    }
}
