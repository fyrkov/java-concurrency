package deadlock;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        String firstMonitor = "first monitor";
        String secondMonitor = "second monitor";
        CountDownLatch countDownLatch = new CountDownLatch(2);

        executorService.submit(new StickingThread(firstMonitor, secondMonitor, countDownLatch));
        executorService.submit(new StickingThread(secondMonitor, firstMonitor, countDownLatch));

    }

    public static class StickingThread implements Runnable {

        final Object first;
        final Object second;
        final CountDownLatch countDownLatch;

        public StickingThread(Object first, Object second, CountDownLatch countDownLatch) {
            this.first = first;
            this.second = second;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            synchronized (second) {
                System.out.println(Thread.currentThread().getName() + " acquired " + second.toString());
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    // ignored
                }
                System.out.println(Thread.currentThread().getName() + " awaits for " + first.toString());
                synchronized (first) {
                    System.out.println(Thread.currentThread().getName() + " acquired " + second.toString() + ". This should not happen");
                }
            }
        }
    }

}
