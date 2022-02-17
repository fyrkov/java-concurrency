package fork_join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/*
The task returns a Fibonacci number at place N.
A Task for the ForkJoinPool must extend the RecursiveTask<T>
 */
public class Fibonacci extends RecursiveTask<Integer> {

    public static void main(String[] args) {
        // It is recommended to use the Common Pool
        final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        final Fibonacci task = new Fibonacci(7);
        final Integer result = forkJoinPool.invoke(task);
        System.out.println(result);
    }

    private final int n;

    public Fibonacci(final int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        System.out.println("Computing number #" + n + " on thread " + Thread.currentThread().getName());
        if (n <= 2) {
            return 1;
        }
        final Fibonacci f1 = new Fibonacci(n - 1);
        final Fibonacci f2 = new Fibonacci(n - 2);

        // fork() asynchronously submits a task to the ForkJoinPool but does not trigger execution
        final ForkJoinTask<Integer> fork1 = f1.fork();
        final ForkJoinTask<Integer> fork2 = f2.fork();
        // join() triggers execution
        return fork1.join() + fork2.join();

        // compute() executes the task synchronously on the same thread
        //return f1.compute() + f2.compute();
    }
}
