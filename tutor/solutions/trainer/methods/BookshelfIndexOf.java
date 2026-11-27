import java.util.Scanner;

public class BookshelfIndexOf {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] books = new int[n];
        for (int i = 0; i < n; i++) {
            books[i] = scanner.nextInt();
        }
        int target = scanner.nextInt();
        int index = indexOf(books, target);
        if (index == -1) {
            System.out.println("Книги " + target + " нет на полке");
        } else {
            System.out.println("Книга " + target + " стоит на месте с индексом " + index);
        }
    }

    static int indexOf(int[] books, int target) {
        for (int i = 0; i < books.length; i++) {
            if (books[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
