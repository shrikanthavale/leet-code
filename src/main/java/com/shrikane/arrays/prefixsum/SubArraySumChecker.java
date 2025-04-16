package com.shrikane.arrays.prefixsum;

/**
 * Given an integer array nums, an array queries where queries[i] = [x, y] and an integer limit, return a boolean
 * array that represents the answer to each query. A query is true if the sum of the subarray from x to y is less
 * than limit, or false otherwise.
 */
public class SubArraySumChecker {

    private SubArraySumChecker() {
        // private constructor
    }

    /**
     * Given an integer array nums, an array queries where queries[i] = [x, y] and an integer limit, return a boolean
     * array that represents the answer to each query. A query is true if the sum of the subarray from x to y is less
     * than limit, or false otherwise.
     *
     * @param nums    nums array.
     * @param queries queries.
     * @param limit   limit.
     * @return boolean array.
     */
    public static boolean[] checkSubArraySum(int[] nums, int[][] queries, int limit) {

        if (queries.length == 0) {
            return new boolean[0];
        }

        // first calculate the sum of the given array
        int[] sumArray = new int[nums.length];
        boolean[] booleanArray = new boolean[queries.length];
        sumArray[0] = nums[0];

        for (int temp = 1; temp < nums.length; temp++) {
            sumArray[temp] = sumArray[temp - 1] + nums[temp];
        }

        for (int temp = 0; temp < queries.length; temp++) {
            int i = queries[temp][0];
            int j = queries[temp][1];

            booleanArray[temp] = sumArray[j] - sumArray[i] + nums[i] > limit;
        }

        return booleanArray;
    }
}
