public class ParallelTestsCounterFix {
    static int passed = 0;

    static synchronized void markPassed() {
        passed++;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] runners = new Thread[4];
        for (int i = 0; i < runners.length; i++) {
            runners[i] = new Thread(() -> {
                for (int t = 0; t < 250_000; t++) {
                    markPassed();
                }
            });
            runners[i].start();
        }
        for (Thread runner : runners) {
            runner.join();
        }
        System.out.println("Раннеров: " + runners.length);
        System.out.println("Пройдено тестов: " + passed);
    }
}
