package synchronizers.countdown_latch;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class Main {

    // In this example two CountDownLatches are used to synchronize
    // start and stop times of the main thread and test task threads
    public static long timeTasks(Runnable task, int nThreads) {

        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(nThreads);

        IntStream.rangeClosed(1, nThreads).forEach(i ->
                new Thread(() -> {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        // ignored
                    }
                }).start());

        long start = System.nanoTime();
        startGate.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
            // ignored
        }
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        timeTasks(() ->
                System.out.println("Test thread " + Thread.currentThread().getName()), 2);
    }

}
