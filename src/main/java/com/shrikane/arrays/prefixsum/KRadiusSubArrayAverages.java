package com.shrikane.arrays.prefixsum;

public class KRadiusSubArrayAverages {

    private KRadiusSubArrayAverages() {
        // private constructor to avoid initialization
    }

    public static int[] bruteForce(int[] inputArray, int k) {

        if (inputArray == null || inputArray.length == 0) {
            return new int[0];
        }

        int[] averages = new int[inputArray.length];

        for (int i = 0; i < inputArray.length; i++) {
            if (i - k < 0 || i + k >= inputArray.length) {
                averages[i] = -1;
                continue;
            }

            int sum = 0;
            for (int j = i - k; j <= i + k; j++) {
                sum += inputArray[j];
            }

            averages[i] = sum / (2 * k + 1);
        }

        return averages;
    }

    public static long[] kRadiusAverages(int[] inputArray, int k) {
        if (inputArray == null || inputArray.length == 0) {
            return new long[0];
        }

        long[] sums = new long[inputArray.length];
        sums[0] = inputArray[0];
        // calculate the sum
        for (int i = 1; i < inputArray.length; i++) {
            sums[i] = sums[i - 1] + inputArray[i];
        }

        long[] averages = new long[inputArray.length];
        for (int i = 0; i < sums.length; i++) {
            if (i - k < 0 || i + k >= inputArray.length) {
                averages[i] = -1;
                continue;
            }

            long sum = 0;
            if (i - k == 0) {
                sum = sums[i + k];
            } else {
                sum = sums[i + k] - sums[i - k - 1];
            }

            averages[i] = sum / (2 * k + 1);
        }

        return averages;
    }
}
