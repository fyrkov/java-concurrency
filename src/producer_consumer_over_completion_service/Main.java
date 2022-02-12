package producer_consumer_over_completion_service;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // CompletionService = ExecutorService + BlockingQueue
        // It allows submitting callable tasks and offers queue-like methods to retrieve results
        // It is reasonable to create a private disposable CompletionServices providing a common ExecutorService
        CompletionService<Pair> completionService = new ExecutorCompletionService<>(executor);

        IntStream.rangeClosed(1, 100).forEach(n -> completionService.submit(() -> isPrime(n)));

        IntStream.rangeClosed(1, 100).forEach(n -> {
            try {
                // take() VS poll():
                // take() blocks until queue has a future
                // poll() returns null if there is no future in Queue.
                Future<Pair> future = completionService.take();
                // Here main thread blocks 2nd time until future is resolved
                Pair pair = future.get();
                String not = pair.isPrime ? "" : " not";
                System.out.println(pair.n +  " is" + not + " a prime");
            } catch (InterruptedException | ExecutionException e) {
                // ignored
            }
        });

        // In general, the ExecutorService will not stop automatically when there is no task to process.
        // It will stay alive and wait for new work to do.
        // shutdown() will make the ExecutorService stop accepting new tasks and shut down after all running threads finish their current work.
        executor.shutdown();
    }


    private static Main.Pair isPrime(final int n) {
        System.out.println("Checking if " + n + " is a prime");
        for (int i = 2; i < n; i++) {
            if (n % i == 0)
                return new Main.Pair(n, false);
        }
        return new Main.Pair(n, true);
    }

    static final class Pair {
        int n;
        boolean isPrime;
        public Pair(final int n, final boolean isPrime) {
            this.n = n;
            this.isPrime = isPrime;
        }
    }
}
