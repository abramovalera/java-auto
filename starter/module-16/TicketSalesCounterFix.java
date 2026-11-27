// Четыре кассы параллельно продают билеты на фестиваль — каждая по 250 000,
// и каждый проданный билет увеличивает общий счётчик sold.
// Ожидается: «Продано билетов: 1000000». Но программа врёт —
// и каждый раз по-разному. Найди и почини ошибки.
public class TicketSalesCounterFix {
    static int sold = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread[] desks = new Thread[4];
        for (int i = 0; i < desks.length; i++) {
            desks[i] = new Thread(() -> {
                for (int t = 0; t < 250_000; t++) {
                    sold++;
                }
            });
            desks[i].start();
        }
        desks[0].join();
        System.out.println("Касс: " + desks.length);
        System.out.println("Продано билетов: " + sold);
    }
}
