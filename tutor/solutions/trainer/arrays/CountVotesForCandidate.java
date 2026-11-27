import java.util.Scanner;

public class CountVotesForCandidate {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] votes = new int[n];
        for (int i = 0; i < n; i++) {
            votes[i] = scanner.nextInt();
        }
        int candidate = scanner.nextInt();
        int count = 0;
        for (int v : votes) {
            if (v == candidate) {
                count++;
            }
        }
        System.out.println("Голосов за кандидата " + candidate + ": " + count);
    }
}
