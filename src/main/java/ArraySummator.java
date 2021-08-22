import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ArraySummator extends RecursiveTask<Integer> {
    protected int[] array;
    protected int numberOfFragments;

    public ArraySummator(int[] array) {
        this.array = array;
        numberOfFragments = 2;
    }

    @Override
    protected Integer compute() {
        if (array.length <= numberOfFragments) {
            // System.out.printf("Task %s execute in thread %s%n", this, Thread.currentThread().getName());
            int sum = 0;
            for (int element : array) {
                sum += element;
            }
            return sum;
        } else
            return forkTasksAndGetResult();
    }

    private int forkTasksAndGetResult() {
        ArraySummator task1 = new ArraySummator(Arrays.copyOfRange(array, 0, array.length / numberOfFragments));
        ArraySummator task2 = new ArraySummator(Arrays.copyOfRange(array, array.length / numberOfFragments, array.length));
        invokeAll(task1, task2);
        return task1.join() + task2.join();
    }
}
