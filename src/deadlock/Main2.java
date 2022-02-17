package deadlock;

import java.util.concurrent.CountDownLatch;

public class Main2 {

    public static void main(String[] args) throws InterruptedException {

        Object o1 = new Object();
        Object o2 = new Object();

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        Runnable run1 = () -> {
            synchronized (o1) {
                countDownLatch.countDown();
                System.out.println("Thread 1 acquired lock 1");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println("this should not happen");
                }
            }
        };

        Runnable run2 = () -> {
            synchronized (o2) {
                countDownLatch.countDown();
                System.out.println("Thread 2 acquired lock 2");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println("this should not happen");
                }
            }
        };

        final Thread thread1 = new Thread(run1);
        thread1.start();
        final Thread thread2 = new Thread(run2);
        thread2.start();

        thread2.join();
    }
}
