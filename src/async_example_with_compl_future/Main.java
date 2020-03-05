package async_example_with_compl_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private final ExecutorService syncJobExecutor = Executors.newFixedThreadPool(10);

    public void syncImagesForAllUsers() {
        long start = System.currentTimeMillis();
        log.info("Sync images job has been started");
        final List<User> users = findAll();

        CompletableFuture.allOf(users.stream()
                // submitting tasks and executing on a thread pool of size 10 for network IO
                .map(user -> CompletableFuture.supplyAsync(() -> googleFacadeClient.getPhoto(user.getEmail()), syncJobExecutor)
                        .thenAccept(photo -> {
                            log.debug("Sync images job storing photo for user {}", user.getEmail());
                            user.setPhoto(photo);
                            userRepository.save(user);
                        })
                        .whenComplete((user1, ex) -> {
                            if (ex != null) {
                                log.warn("Sync images job failed to fetch photo for user {}", user.getEmail(), ex);
                            }
                        })
                ).toArray(CompletableFuture[]::new))
                .whenComplete((aVoid, throwable) -> log.info("Sync images job finished in {} ms", millisAfter(start)));
    }
}
