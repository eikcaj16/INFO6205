package edu.neu.coe.info6205.union_find;

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
        final UF_HWQUPC uf_hwqupc = new UF_HWQUPC(n);

        while (uf_hwqupc.components() != 1) {
            i = random.nextInt(n);
            j = random.nextInt(n);
            if (!uf_hwqupc.connected(i, j)) {
                uf_hwqupc.union(i, j);
                connections++;
            }
        }

        return connections;
    }

    /**
     * Call UFClient.count(int n) for given times
     * by using the doubling method
     *
     * @param startN the initial number of sites
     * @param times
     * @return a list of pairs of n and m
     */
    public static List<Pair> multiCount(int startN, int times) {
        List<Pair> n_m_s = new ArrayList<>();

        for (int i = 0; i < times; i++) {
            n_m_s.add(new Pair(startN, count(startN)));
            startN *= 2;
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
     * when there is two input arguments
     *
     * @param args arguments from CLI
     */
    public static void main(String[] args) {
        int n = 0;
        if (args.length == 1) {
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }

            System.out.println(">>>>>> n: " + n + "\tm: " + count(n));
        } else if (args.length == 2) {
            int times = 0;
            try {
                n = Integer.parseInt(args[0]);
                times = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " and " + args[1] + " must be integers");
                System.exit(1);
            }
            
            List<Pair> n_m_s = multiCount(n, times);

            System.out.println("n\t\tm\n================================");
            n_m_s.stream().forEach(n_m -> System.out.println(n_m.a + "\t\t" + n_m.b));
        } else {
            throw new IllegalArgumentException("Please input one or two arguments.");
        }
    }
}
