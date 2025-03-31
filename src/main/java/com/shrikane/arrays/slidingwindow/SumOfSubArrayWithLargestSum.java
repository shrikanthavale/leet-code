package com.shrikane.arrays.slidingwindow;

/**
 * Given an integer array nums and an integer k, find the sum of the subarray with the largest sum whose length is k.
 */
public class SumOfSubArrayWithLargestSum {

    private SumOfSubArrayWithLargestSum() {
        // private constructor
    }

    /**
     * Given an integer array nums and an integer k, find the sum of the subarray with the largest sum whose
     * length is k.
     *
     * @param arrayToInvestigate  array to investigate.
     * @param maxLengthOfSubArray allowed max length of sub array.
     * @return max sum.
     */
    public static int sumOfSubArrayWithLargestSumHavingLength(int[] arrayToInvestigate, int maxLengthOfSubArray) {
        int left = 0;
        int right = 0;
        int sum = 0;
        int maxSum = 0;

        for (; right < arrayToInvestigate.length; right++) {
            sum = sum + arrayToInvestigate[right];

            if (right - left + 1 == maxLengthOfSubArray) {
                maxSum = Math.max(maxSum, sum);

                sum = sum - arrayToInvestigate[left];
                left++;
            }
        }

        return maxSum;
    }
}
