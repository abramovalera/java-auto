import java.util.Scanner;

public class BookCardPrinter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();
        String author = scanner.nextLine();
        int pages = scanner.nextInt();
        LibraryBook book = new LibraryBook(title, author, pages);
        System.out.println(book.describe());
        System.out.println("Толстая книга: " + book.isThick());
    }
}

class LibraryBook {
    String title;
    String author;
    int pages;

    LibraryBook(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    String describe() {
        return "«" + title + "», " + author + ", " + pages + " стр.";
    }

    boolean isThick() {
        return pages > 300;
    }
}
