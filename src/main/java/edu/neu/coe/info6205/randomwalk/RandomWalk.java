/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.randomwalk;

import java.util.ArrayList;
import java.util.Random;

public class RandomWalk {

    private int x = 0;
    private int y = 0;

    private final Random random = new Random();

    /**
     * Private method to move the current position, that's to say the drunkard moves
     *
     * @param dx the distance he moves in the x direction
     * @param dy the distance he moves in the y direction
     */
    private void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
     * Perform a random walk of m steps
     *
     * @param m the number of steps the drunkard takes
     */
    private void randomWalk(int m) {
        for (int i = 0; i < m; i++) {
            randomMove();
        }
    }

    /**
     * Private method to generate a random move according to the rules of the situation.
     * That's to say, moves can be (+-1, 0) or (0, +-1).
     */
    private void randomMove() {
        boolean ns = random.nextBoolean();
        int step = random.nextBoolean() ? 1 : -1;
        move(ns ? step : 0, ns ? 0 : step);
    }

    /**
     * Method to compute the distance from the origin (the lamp-post where the drunkard starts) to his current position.
     *
     * @return the (Euclidean) distance from the origin to the current position.
     */
    public double distance() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * Perform multiple random walk experiments, returning the mean distance.
     *
     * @param m the number of steps for each experiment
     * @param n the number of experiments to run
     * @return the mean distance
     */
    public static double randomWalkMulti(int m, int n) {
        double totalDistance = 0;
        for (int i = 0; i < n; i++) {
            RandomWalk walk = new RandomWalk();
            walk.randomWalk(m);
            totalDistance = totalDistance + walk.distance();
        }
        return totalDistance / n;
    }

    public static void variousStep(int m0, int m1, int n, ArrayList<double[]> res) {
        for (int m = m0; m < m1 + 1; m++) {
            double meanDistance = randomWalkMulti(m, n);
            System.out.println(m + " steps: " + meanDistance + " over " + n + " experiments (c = " + meanDistance / Math.sqrt(m) + ")");
            res.add(new double[]{(double) m, meanDistance, meanDistance / Math.sqrt(m)});  // {steps, meanDistance, c}
        }
    }

    public static void main(String[] args) {
        // Step 1: Do an experiment for observing.
        ArrayList<double[]> res1 = new ArrayList<>();
        variousStep(1, 50, 1000, res1);
        res1.forEach(r -> System.out.println(r[0] + "\t" + r[1]));

        // Step 3: Do an experiment to obtain an average value of c.
        ArrayList<double[]> res2 = new ArrayList<>();
        variousStep(2, 50, 1000, res2);
        res2.forEach(r -> System.out.println(r[0] + "\t" + r[1] + "\t" + r[2]));
        double c_avg = res2.stream().mapToDouble(c -> c[2]).average().orElse(0.0);
        System.out.println(">>>>>>>>>> c_avg = " + c_avg);

        // Step 4: Do an experiment to prove the assumption is acceptable.
        double c_avg_4 = 0.8883323419656484;
        ArrayList<Double> sds = new ArrayList<>();
        ArrayList<double[]> res3 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            res3.clear();
            variousStep(2, 50, 1000, res3);
            double sd = Math.sqrt(res3.stream().mapToDouble(c -> Math.pow(c[2] - c_avg_4, 2)).sum() / (res3.size() - 1));
            System.out.println(">>> SD: " + sd);
            sds.add(sd);
        }
        System.out.println(">>> SD_avg: " + sds.stream().mapToDouble(sd -> sd).average().orElse(0.0));
    }

}
