package producer_consumer_over_semaphore;

import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Main {

    public static final int CAPACITY = 5;

    public static void main(String[] args) throws InterruptedException {

        /*
        With 2 semaphores consumers can apply back pressure on producers
         */

        Semaphore permitToConsume = new Semaphore(0);
        Semaphore permitToProduce = new Semaphore(CAPACITY);
        Queue<String> q = new ConcurrentLinkedQueue<>();

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        executorService.invokeAll(List.of(
                new Producer(1, permitToConsume, permitToProduce, q),
                new Producer(2, permitToConsume, permitToProduce, q),
                new Producer(3, permitToConsume, permitToProduce, q),

                new Consumer(1, permitToConsume, permitToProduce, q),
                new Consumer(2, permitToConsume, permitToProduce, q),
                new Consumer(3, permitToConsume, permitToProduce, q),
                new Consumer(4, permitToConsume, permitToProduce, q),
                new Consumer(5, permitToConsume, permitToProduce, q)
        ));
    }


    static abstract class PC implements Callable<Void> {

        final int number;
        final Semaphore permitToConsume;
        final Semaphore permitToProduce;
        final Queue<String> q;

        public PC(final int number, final Semaphore permitToConsume, final Semaphore permitToProduce, final Queue<String> q) {
            this.number = number;
            this.permitToConsume = permitToConsume;
            this.permitToProduce = permitToProduce;
            this.q = q;
        }

        @Override
        public abstract Void call();
    }

    static class Producer extends PC {

        public Producer(final int number, final Semaphore permitToConsume, final Semaphore permitToProduce, final Queue<String> q) {
            super(number, permitToConsume, permitToProduce, q);
        }

        @Override
        public Void call() {
            while (true) {
                try {
                    permitToProduce.acquire();

                    Thread.sleep(1000L);
                    final String item = UUID.randomUUID().toString().substring(0, 3);
                    q.offer(item);
                    System.out.println("Producer " + number + " produced an item: " + item);

                    permitToConsume.release();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    static class Consumer extends PC {

        public Consumer(final int number, final Semaphore permitToConsume, final Semaphore permitToProduce, final Queue<String> q) {
            super(number, permitToConsume, permitToProduce, q);
        }

        @Override
        public Void call() {
            while (true) {
                try {
                    permitToConsume.acquire();

                    final String item = q.poll();
                    Thread.sleep(1000L);
                    System.out.println("Consumer " + number + "  consumed an item: " + item);

                    permitToProduce.release();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}

