package course;

public class NewThread {

    public static void main(String[] args) {

        final Thread thread = new Thread(() -> {
            System.out.println("Inside thread: " + Thread.currentThread().getName());
            throw new RuntimeException("intentional exception");
        });

        // Catching uncaught exceptions in a spawned thread
        thread.setUncaughtExceptionHandler((thread1, throwable) -> {
            System.out.println("Uncaught exception: " + throwable);
        });

        thread.setName("Test thread");
        thread.start();
    }
}
