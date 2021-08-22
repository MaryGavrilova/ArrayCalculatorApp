import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ArrayArithmeticAverageCounter extends RecursiveTask<Double> {
    protected int[] array;
    protected int initialArrayLength;
    protected int numberOfFragments;


    public ArrayArithmeticAverageCounter(int[] array, int initialArrayLength) {
        this.array = array;
        this.initialArrayLength = initialArrayLength;
        numberOfFragments = 2;
    }

    @Override
    protected Double compute() {
        if (array.length <= numberOfFragments) {
            // System.out.printf("Task %s execute in thread %s%n", this, Thread.currentThread().getName());
            double sum = 0;
            for (int element : array) {
                sum += (double) element / initialArrayLength;
            }
            return sum;
        } else
            return forkTasksAndGetResult();
    }

    private Double forkTasksAndGetResult() {
        ArrayArithmeticAverageCounter task1 = new ArrayArithmeticAverageCounter(Arrays.copyOfRange(array, 0, array.length / numberOfFragments), initialArrayLength);
        ArrayArithmeticAverageCounter task2 = new ArrayArithmeticAverageCounter(Arrays.copyOfRange(array, array.length / numberOfFragments, array.length), initialArrayLength);
        invokeAll(task1, task2);
        return task1.join() + task2.join();
    }
}
