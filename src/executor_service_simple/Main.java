package executor_service_simple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<Pair>> futures = new ArrayList<>();

        IntStream.rangeClosed(100, 200).forEach(n -> {
            Future<Pair> result = executor.submit(() -> isPrime(n));
            futures.add(result);
        });

        // In general, the ExecutorService will not stop automatically when there is not task to process.
        // It will stay alive and wait for new work to do.
        // shutdown() will make the ExecutorService stop accepting new tasks and shut down after all running threads finish their current work.
        executor.shutdown();
    }

    private static Pair isPrime(final int n) {
        System.out.println("Checking if " + n + " is a prime");
        for (int i = 2; i < n; i++) {
            if (n % i == 0)
                return new Pair(n, false);
        }
        return new Pair(n, true);
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
