package fork_join;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinUsingRecursion extends RecursiveTask<List<String>> {

    private List<String> input;

    public ForkJoinUsingRecursion(final List<String> input) {
        this.input = input;
    }

    public static void main(String[] args) {
        final List<String> names = List.of("Mabel", "Dipper", "Stan");
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinUsingRecursion forkJoinUsingRecursion = new ForkJoinUsingRecursion(names);
        final List<String> result = forkJoinPool.invoke(forkJoinUsingRecursion);
        result.forEach(System.out::println);
    }

    @Override
    protected List<String> compute() {
        if (input.size() <= 1) {
            List<String> resultList = new ArrayList<>();
            input.forEach(name -> resultList.add(name.length() + " " + name));
            return resultList;
        }
        int midpoint = input.size() / 2;
        final ForkJoinTask<List<String>> leftInputList = new ForkJoinUsingRecursion(input.subList(0, midpoint)).fork();
        input = input.subList(midpoint, input.size());
        final List<String> rightResult = compute();
        final List<String> leftResult = leftInputList.join();
        leftResult.addAll(rightResult);
        return leftResult;
    }
}
