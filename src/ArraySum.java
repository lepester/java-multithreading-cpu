import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

public class ArraySum {
    private static final Random RAND = new Random(42);   // random number generator

    public static void main(String[] args) {
        int LENGTH = 100000;   // initial length of array to sort
        int RUNS = 10;   // how many times to grow by 2?

        for (int i = 1; i <= RUNS; i++) {
            int[] a = createRandomArray(LENGTH);

            // run the algorithm and time how long it takes
            long startTime1 = System.currentTimeMillis();
            LongAdder total = new LongAdder();
            for (int j = 1; j <= 100; j++) {
                total.add(sumOfAllElementsAnyThread(a, 8) - total.sum());
            }
            long endTime1 = System.currentTimeMillis();

            System.out.printf("%10d elements  =>  %6d ms \n", LENGTH, endTime1 - startTime1);
            LENGTH *= 2.2;   // double size of array for next time
        }
    }


    // Computes the total sum of all elements of the given array.
    // It can make use of as many cores/CPUs as you want to give it.
    public static int sumOfAllElementsAnyThread(int[] a, int threadCount) {
        int len = (int) Math.ceil(1.0 * a.length / threadCount);
        Summer[] summers = new Summer[threadCount];
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            summers[i] = new Summer(a, i * len, Math.min((i + 1) * len, a.length));
            threads[i] = new Thread(summers[i]);
            threads[i].start();
        }
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }

        int total = 0;
        for (Summer summer : summers) {
            total += summer.getSum();
        }
        return total;
    }

    // helper method to compute sum of array, indexes [min .. max).
    public static int sumRange(int[] a, int min, int max) {
        int result = 0;
        for (int i = min; i < max; i++) {
            result += a[i];
        }
        return result;
    }

    // Creates an array of the given length, fills it with random
    // non-negative integers, and returns it.
    public static int[] createRandomArray(int length) {
        int[] a = new int[length];
        for (int i = 0; i < a.length; i++) {
            a[i] = RAND.nextInt(50);
        }
        return a;
    }
}