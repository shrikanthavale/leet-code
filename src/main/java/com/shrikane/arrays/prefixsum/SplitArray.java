package com.shrikane.arrays.prefixsum;

/**
 * Given an integer array nums, find the number of ways to split the array into two parts so that the first section
 * has a sum greater than or equal to the sum of the second section. The second section should have at least one number.
 */
public class SplitArray {

    private SplitArray() {
        // private constructor to prevent instantiation
    }

    /**
     * Given an integer array nums, find the number of ways to split the array into two parts so that the first section
     * has a sum greater than or equal to the sum of the second section. The second section should have at least
     * one number.
     *
     * @param arr integer array to process.
     * @return number of ways to split array.
     */
    public static int numberOfWaysToSplitArray(int[] arr) {

        if (arr.length == 0) {
            return 0;
        }

        int[] prefixSum = new int[arr.length];
        prefixSum[0] = arr[0];

        for (int i = 1; i < arr.length; i++) {
            prefixSum[i] = prefixSum[i - 1] + arr[i];
        }

        int numberOfWaysToSplitArray = 0;
        for (int i = 0; i < prefixSum.length - 1; i++) {
            if (prefixSum[i] >= (prefixSum[prefixSum.length - 1] - prefixSum[i])) {
                numberOfWaysToSplitArray++;
            }
        }

        return numberOfWaysToSplitArray;
    }
}
