package edu.neu.coe.info6205.union_find;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * A UF("union-find") client
 *
 * @author Yiqing Huang
 */
public class UFClient {
    /**
     * Randomly union any pair of sites until there is
     * only one component
     *
     * @param n the number of sites
     * @return the number of collections
     */
    public static int count(int n) {
        int connections = 0;
        int i, j;
        final Random random = new Random();
        final UF_HWQUPC uf_hwqupc = new UF_HWQUPC(n, true);

        while (uf_hwqupc.components() != 1) {
            connections++;
            i = random.nextInt(n);
            j = random.nextInt(n);
            if (!uf_hwqupc.connected(i, j)) {
                uf_hwqupc.union(i, j);
            }
        }

        return connections;
    }

    /**
     * Call UFClient.count(int n) for multiple times in range of {@code [start, end)}
     * with step{@code step}
     *
     * @param start the initial number of sites
     * @param end the end number of sites
     * @param step step over
     * @return a list of pairs of n and m
     */
    public static List<Pair> multiCount(int start, int end, int step) {
        List<Pair> n_m_s = new ArrayList<>();

        for (int i = start; i < end; i += step) {
            n_m_s.add(new Pair(i, count(i)));
        }

        return n_m_s;
    }

    /**
     * The main function of UFClient
     *
     * Call UFClient.count(int n) when there is
     * one input argument
     *
     * Call UFClient.multiCount(int n, int times)
     * when there is two input arguments and save
     * the result into a csv file
     *
     * @param args arguments from CLI
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            int n = 0;
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }

            System.out.println(">>>>>> n: " + n + "\tm: " + count(n));
        } else if (args.length == 3) {
            int start = 0;
            int end = 0;
            int step = 0;

            try {
                start = Integer.parseInt(args[0]);
                end = Integer.parseInt(args[1]);
                step = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + ", " + args[1] + " and " + args[2] + " must be integers");
                System.exit(1);
            }
            
            List<Pair> n_m_s = multiCount(start, end, step);

            System.out.println("n\t\tm\n================================");
            n_m_s.stream().forEach(n_m -> System.out.println(n_m.a + "\t\t" + n_m.b));

            try (
                    FileWriter fw = new FileWriter("raw-data.csv");
                    BufferedWriter out = new BufferedWriter(fw);
                 ) {
                out.write("n, m\n");
                n_m_s.stream().forEach(n_m -> {
                    try {
                        out.write(String.format("%d, %d\n", n_m.a, n_m.b));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Please input one or three arguments.");
        }
    }
}
