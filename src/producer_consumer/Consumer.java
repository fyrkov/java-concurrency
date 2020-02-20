package producer_consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Consumer implements Runnable {

    private final BlockingQueue<Object> queue;

    public Consumer(BlockingQueue<Object> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Blocking retrieval
                Object o = queue.take();
                Thread.sleep(ThreadLocalRandom.current().nextInt(1, 1000));
            }
        } catch (InterruptedException e) {
            // Restoring the interrupted status after catching InterruptedException,
            // see https://www.ibm.com/developerworks/java/library/j-jtp05236/index.html
            Thread.currentThread().interrupt();
        }
    }
}
