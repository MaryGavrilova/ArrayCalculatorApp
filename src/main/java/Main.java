import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        // Вывод по результатам эксперементов с количеством элементов в массиве:
        // при маленьком количестве элементов в массиве эффективность:
        // 1 место - цикл for-each - однопоточный подсчет
        // 2 место - многопоточный подсчет (разбивка на 2 задачи)
        // 3 место - Stream API - однопоточный подсчет
        // при большом количестве элементов в массиве эффективность:
        // 1 место - цикл for-each - однопоточный подсчет
        // 2 место - Stream API - однопоточный подсчет
        // 3 место - многопоточный подсчет (разбивка на 2 задачи)

        int numberOfArrayElements = 100000;
        int upperBorder = 50;
        int lowerBorder = 0;

        // создание массива целых чисел для экспериментов
        int[] arrayForExperiments = generateArray(numberOfArrayElements, upperBorder, lowerBorder);
        System.out.println(Arrays.toString(arrayForExperiments));

        // однопоточный подсчет суммы элементов массива
        // 1 вариант: Java 8 Stream API
        long startTime1 = System.currentTimeMillis();

        int sum1 = sumBySingleThreadCountingFirstVariant(arrayForExperiments);
        double arithmeticAverage1 = countArithmeticAverageFirstVariant(arrayForExperiments);

        long endTime1 = System.currentTimeMillis();
        System.out.printf("Однопоточный подсчет: 1 вариант: сумма равна %s, \nсреднее арифметическое значение равно %.2f\n",
                sum1, arithmeticAverage1);
        System.out.printf("Время выполнения: Однопоточный подсчет: 1 вариант: %s миллисекунд\n", (endTime1 - startTime1));

        System.out.println("************************************************\n");
        // 2 вариант: for-each цикл
        long startTime2 = System.currentTimeMillis();

        int sum2 = sumBySingleThreadCountingSecondVariant(arrayForExperiments);
        double arithmeticAverage2 = countArithmeticAverageSecondVariant(arrayForExperiments);

        long endTime2 = System.currentTimeMillis();
        System.out.printf("Однопоточный подсчет: 2 вариант: сумма равна %s, \nсреднее арифметическое значение равно %.2f\n",
                sum2, arithmeticAverage2);
        System.out.printf("Время выполнения: Однопоточный подсчет: 2 вариант: %s миллисекунд\n", (endTime2 - startTime2));


        System.out.println("************************************************\n");
        // многопоточный подсчет суммы элементов массива
        long startTime = System.currentTimeMillis();

        ArraySummator arraySummator = new ArraySummator(arrayForExperiments);
        ForkJoinPool forkJoinPool1 = new ForkJoinPool();
        int sum = forkJoinPool1.invoke(arraySummator);

        ArrayArithmeticAverageCounter arrayArithmeticAverageCounter = new ArrayArithmeticAverageCounter(arrayForExperiments, arrayForExperiments.length);
        ForkJoinPool forkJoinPool2 = new ForkJoinPool();
        double arithmeticAverage = forkJoinPool2.invoke(arrayArithmeticAverageCounter);

        long endTime = System.currentTimeMillis();

        System.out.printf("Многопоточный подсчет: сумма равна %s,\nсреднее арифметическое значение равно %.2f\n",
                sum, arithmeticAverage);
        System.out.printf("Время выполнения: Многопоточный подсчет: %s миллисекунд\n", (endTime - startTime));
    }

    public static int[] generateArray(int numberOfArrayElements, int upperBorder, int lowerBorder) {
        int[] array = new int[numberOfArrayElements];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * (upperBorder - lowerBorder) + lowerBorder);
        }
        return array;
    }

    public static int sumBySingleThreadCountingFirstVariant(int[] array) {
        return Arrays.stream(array).sum();
    }

    public static int sumBySingleThreadCountingSecondVariant(int[] array) {
        int sum = 0;
        for (int element : array) {
            sum += element;
        }
        return sum;
    }

    public static double countArithmeticAverageFirstVariant(int[] array) {
        return Arrays.stream(array).average().orElseThrow();
    }

    public static double countArithmeticAverageSecondVariant(int[] array) {
        double sum = 0;
        for (int element : array) {
            sum += (double) element / array.length;
        }
        return sum;
    }
}
