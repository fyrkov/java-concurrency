package completable_future;

import java.util.concurrent.CompletableFuture;

public class CFExample1 {
    public static void main(String[] args) {
        // Runs on the Common ForkJoinPool
        CompletableFuture.supplyAsync(() -> "Some String")
                .exceptionallyAsync(throwable -> "Fallback value")
                .thenApplyAsync(String::toUpperCase)
                .thenAcceptAsync(s -> System.out.println("Working with value " + s + " in " + Thread.currentThread().getName()))
                .join();
    }
}
