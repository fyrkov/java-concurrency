package synchronizers.spider;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

// Spider has eight legs that should move in a defined order
public class Main {

    public static void moveLeg(int n) throws InterruptedException {
        Thread.sleep(100);
        System.out.println("Leg " + n + " moved");
    }

    public static void main(String[] args) throws InterruptedException {
        // Fairness = true guarantees acquiring order
        Semaphore semaphore = new Semaphore(1, true);
        semaphore.acquire();

        IntStream.rangeClosed(1, 8).forEach(i ->
                new Thread(() -> {
                    while (true) {
                        try {
                            semaphore.acquire();
                            moveLeg(i);
                            semaphore.release();
                        } catch (InterruptedException e) {
                            // ignored
                        }
                    }
                }).start());

        semaphore.release();
        blockMainThread();
        return;
    }

    private static void blockMainThread() throws InterruptedException {
        Object o = new Object();
        synchronized (o) {
            o.wait();
        }
    }
}
