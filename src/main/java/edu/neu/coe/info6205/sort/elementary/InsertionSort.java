/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.Config;
import edu.neu.coe.info6205.util.Timer;
import edu.neu.coe.info6205.util.Utilities;

import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.Random;
import java.util.Arrays;
import java.util.function.Function;

public class InsertionSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * Constructor for any sub-classes to use.
     *
     * @param description the description.
     * @param N           the number of elements expected.
     * @param config      the configuration.
     */
    protected InsertionSort(String description, int N, Config config) {
        super(description, N, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(int N, Config config) {
        this(DESCRIPTION, N, config);
    }

    public InsertionSort(Config config) {
        this(new BaseHelper<>(DESCRIPTION, config));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        super(helper);
    }

    public InsertionSort() {
        this(BaseHelper.getHelper(InsertionSort.class));
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();

        BiConsumer<Integer, X[]> insert = (i, xss) -> {
            for (int j = i - 1; j >= 0; j--) {
                if (!helper.swapConditional(xss, j, j + 1)) break;
            }
        };

        for (int i = from; i < to; i++) {
            insert.accept(i, xs);
        }
    }

    public static final String DESCRIPTION = "Insertion sort";

    public static <T extends Comparable<T>> void sort(T[] ts) {
        new InsertionSort<T>().mutatingSort(ts);
    }

    /**
     * Benchmark Insertion Sort Algorithm.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        int trials = 10;
        int n = 100;
        final Random random = new Random();
        final InsertionSort insertionSort = new InsertionSort();
        boolean warmup_flag = true;

        System.out.println("\tn\t\tordered\t\tpartial\t\trandom\t\treverse");
        System.out.println("\t-------------------------------------");

        for (int i = 0; i < trials; i++) {
            if (warmup_flag) i--;

            n = i == 0 ? n : n * 2;
            int finalN = n;

            Integer[] random_array = Utilities.fillRandomArray(Integer.class, random, n, r -> r.nextInt(1000));
            Integer[] ordered_array = Arrays.stream(random_array).sorted().toArray(Integer[]::new);
            // Select a range to be sorted.
            int[] partial_idx = Arrays.stream(new int[2]).map(a -> random.nextInt(finalN)).sorted().toArray();
            Integer[] partially_array = random_array.clone();
            insertionSort.sort(partially_array, partial_idx[0], partial_idx[1]);
            Integer[] reverse_array = Arrays.stream(random_array).sorted(Comparator.reverseOrder()).toArray(Integer[]::new);

            Function<Integer[], Double> benchmark = arr ->
                    new Timer().repeat(100,
                            () -> arr,
                            ar -> {
                                insertionSort.sort(ar, 0, finalN);
                                return null;
                            });

            double remt = benchmark.apply(reverse_array);
            double rmt = benchmark.apply(random_array);
            double pmt = benchmark.apply(partially_array);
            double omt = benchmark.apply(ordered_array);

            if (!warmup_flag)
                System.out.format("\t%d\t\t%,.08f\t\t%,.08f\t\t%,.08f\t\t%,.08f\n", n, omt, pmt, rmt, remt);
            if (warmup_flag) warmup_flag = false;
        }
    }
}