package producer_consumer_over_queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {

        int queueCapacity = 4;
        final BlockingQueue<Object> queue = new LinkedBlockingQueue<>(queueCapacity);


        final Producer producer1 = new Producer(queue);
        final Producer producer2 = new Producer(queue);

        final Consumer consumer1 = new Consumer(queue);
        final Consumer consumer2 = new Consumer(queue);

        new Thread(producer1).start();
        new Thread(producer2).start();
        new Thread(consumer1).start();
        new Thread(consumer2).start();

    }
}
