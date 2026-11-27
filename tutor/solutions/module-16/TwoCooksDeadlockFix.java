// Эталон: оба повара берут инструменты в ОДНОМ порядке — сначала ложку, потом половник.
// Круга ожидания больше нет: кто первым взял ложку, тот возьмёт и половник, второй подождёт.
public class TwoCooksDeadlockFix {
    static final Object spoon = new Object();
    static final Object ladle = new Object();
    static int totalDishes = 0;

    static void cookAnya(int[] myDishes) {
        for (int d = 0; d < 5; d++) {
            synchronized (spoon) {
                sleepQuietly(10);
                synchronized (ladle) {
                    myDishes[0]++;
                    totalDishes++;
                }
            }
        }
    }

    static void cookBoris(int[] myDishes) {
        for (int d = 0; d < 5; d++) {
            synchronized (spoon) {          // было: сначала ladle — отсюда и взаимная блокировка
                sleepQuietly(10);
                synchronized (ladle) {
                    myDishes[0]++;
                    totalDishes++;
                }
            }
        }
    }

    static void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] anya = new int[1];
        int[] boris = new int[1];
        Thread cookA = new Thread(() -> cookAnya(anya));
        Thread cookB = new Thread(() -> cookBoris(boris));
        cookA.start();
        cookB.start();
        cookA.join();
        cookB.join();
        System.out.println("Аня приготовила блюд: " + anya[0]);
        System.out.println("Борис приготовил блюд: " + boris[0]);
        System.out.println("Всего блюд: " + totalDishes);
    }
}
