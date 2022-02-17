package synchronizers.spider;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

// Spider has eight legs that should move in a defined order
public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Fairness = true guarantees acquiring order
        Semaphore semaphore = new Semaphore(1, true);
        semaphore.acquire();

        IntStream.rangeClosed(1, 8).forEach(i ->
                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            semaphore.acquire();
                            moveLeg(i);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            semaphore.release();
                        }
                    }
                }).start());

        semaphore.release();
        blockMainThread();
    }

    private static void moveLeg(int n) throws InterruptedException {
        Thread.sleep(200);
        System.out.println("Leg " + n + " moved");
    }

    private static synchronized void blockMainThread() throws InterruptedException {
        Object o = new Object();
        synchronized (o) {
            o.wait();
        }
    }
}
