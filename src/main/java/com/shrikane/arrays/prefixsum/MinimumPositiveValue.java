package com.shrikane.arrays.prefixsum;

/**
 * Utility class to determine the smallest positive starting value such that the running sum of the array
 * (beginning with this starting value) remains positive throughout the traversal.
 */
public class MinimumPositiveValue {

    private MinimumPositiveValue() {
        // private constructor
    }

    /**
     * Determines the smallest positive starting value such that the running sum of the array
     * (beginning with this starting value) remains positive throughout the traversal.
     *
     * @param inputArray the input array of integers for which the minimum positive starting value is calculated.
     *                   If the array is null or empty, the method returns 1.
     * @return the smallest positive integer starting value that ensures the running sum remains positive.
     */
    public static int efficientWayForMinimumPositiveValue(int[] inputArray) {

        if (inputArray == null || inputArray.length == 0) {
            return 1;
        }

        int startValue = 1;
        while (!checkForMinimum(inputArray, startValue)) {
            startValue++;
        }

        return startValue;
    }

    /**
     * Determines the smallest positive integer such that the running sum of the array
     * (beginning with this starting value) remains positive throughout the traversal.
     *
     * @param inputArray an array of integers for which the minimum positive starting value is calculated.
     *                   If the array is null or empty, the method returns 1.
     * @return the smallest positive integer starting value that ensures the running sum remains positive.
     */
    public static int minimumPositiveValue(int[] inputArray) {
        if (inputArray == null || inputArray.length == 0) {
            return 1;
        }

        int[] prefixSum = new int[inputArray.length];
        prefixSum[0] = inputArray[0];

        int startValue = 1;
        if (prefixSum[0] < 1) {
            startValue = 1 - prefixSum[0];
        }

        for (int i = 1; i < inputArray.length; i++) {
            prefixSum[i] = prefixSum[i - 1] + inputArray[i];
            if (prefixSum[i] < 1 && startValue < (1 - prefixSum[i])) {
                startValue = 1 - prefixSum[i];
            }
        }

        return startValue;
    }

    private static boolean checkForMinimum(int[] inputArray, int startValue) {
        int sum = startValue;
        for (int i = 0; i < inputArray.length; i++) {
            sum = sum + inputArray[i];
            if (sum < 1) {
                return false;
            }
        }

        return true;
    }
}
