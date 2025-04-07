package com.shrikane.arrays.slidingwindow;

import static com.shrikane.arrays.slidingwindow.MaximumAverageSubArray.findMaxAverage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * You are given an integer array nums consisting of n elements, and an integer k.
 * <p>
 * Find a contiguous subarray whose length is equal to k that has the maximum average value and return this value.
 * Any answer with a calculation error less than 10-5 will be accepted.
 */
class MaximumAverageSubArrayTest {

    @Test
    void testFindMaxAverage() {
        int[] nums1 = {1, 12, -5, -6, 50, 3};
        int k1 = 4;
        double expected1 = 12.75;
        assertEquals(expected1, findMaxAverage(nums1, k1), 1e-5);

        // Test case 2
        int[] nums2 = {5};
        int k2 = 1;
        double expected2 = 5.0;
        assertEquals(expected2, findMaxAverage(nums2, k2), 1e-5);

        // Test case 3
        int[] nums3 = {-1, -12, -5, -6, -50, -3};
        int k3 = 2;
        double expected3 = -5.5;
        assertEquals(expected3, findMaxAverage(nums3, k3), 1e-5);

        // Test case 4
        int[] nums4 = {0, 4, 0, 3, 2};
        int k4 = 1;
        double expected4 = 4.0;
        assertEquals(expected4, findMaxAverage(nums4, k4), 1e-5);

        // Test case 5
        int[] nums5 = {7, 4, 5, 8, 8, 3, 9, 8, 7, 6};
        int k5 = 7;
        double expected5 = 7.0;
        assertEquals(expected5, findMaxAverage(nums5, k5), 1e-5);
    }
}
