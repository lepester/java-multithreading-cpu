package main;

public class Summer implements Runnable {
    private final int[] a;
    private final int min;
    private final int max;
    private int sum;

    public Summer(int[] a, int min, int max) {
        this.a = a;
        this.min = min;
        this.max = max;
    }

    public int getSum() {
        return sum;
    }

    public void run() {
        this.sum = ArraySum.sumRange(a, min, max);
    }
}