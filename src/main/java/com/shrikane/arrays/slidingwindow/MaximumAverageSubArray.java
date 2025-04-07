package com.shrikane.arrays.slidingwindow;

/**
 * You are given an integer array nums consisting of n elements, and an integer k.
 * <p>
 * Find a contiguous subarray whose length is equal to k that has the maximum average value and return this value.
 * Any answer with a calculation error less than 10-5 will be accepted.
 */
public class MaximumAverageSubArray {

    private MaximumAverageSubArray() {
    }

    public static double findMaxAverage(int[] nums, int k) {
        int left = 0;
        int right = 0;
        double maxAverage = -1000000; //
        float sum = 0;

        for (; right < nums.length; right++) {
            if (right - left + 1 > k) {
                break;
            } else {
                sum = sum + nums[right];
            }
        }

        maxAverage = Math.max(maxAverage, sum / k);

        while (right < nums.length) {
            sum = sum - nums[left];
            left = left + 1;
            sum = sum + nums[right];
            maxAverage = Math.max(maxAverage, sum / k);
            right = right + 1;
        }

        return maxAverage;
    }
}
