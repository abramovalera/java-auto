public class TicketSalesCounterFix {
    static int sold = 0;

    static synchronized void sellTicket() {
        sold++;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] desks = new Thread[4];
        for (int i = 0; i < desks.length; i++) {
            desks[i] = new Thread(() -> {
                for (int t = 0; t < 250_000; t++) {
                    sellTicket();
                }
            });
            desks[i].start();
        }
        for (Thread desk : desks) {
            desk.join();
        }
        System.out.println("Касс: " + desks.length);
        System.out.println("Продано билетов: " + sold);
    }
}
