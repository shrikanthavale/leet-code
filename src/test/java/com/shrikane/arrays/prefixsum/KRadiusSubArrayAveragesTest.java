package com.shrikane.arrays.prefixsum;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class KRadiusSubArrayAveragesTest {

    @Test
    void testExample1() {
        int[] nums = {7, 4, 3, 9, 1, 8, 5, 2, 6};
        int k = 3;
        long[] expected = {-1, -1, -1, 5, 4, 4, -1, -1, -1};
        assertArrayEquals(expected, KRadiusSubArrayAverages.kRadiusAverages(nums, k));
    }

    @Test
    void testExample2() {
        int[] nums = {100000};
        int k = 0;
        long[] expected = {100000};
        assertArrayEquals(expected, KRadiusSubArrayAverages.kRadiusAverages(nums, k));
    }

    @Test
    void testExample3() {
        int[] nums = {8};
        int k = 100000;
        long[] expected = {-1};
        assertArrayEquals(expected, KRadiusSubArrayAverages.kRadiusAverages(nums, k));
    }

    @Test
    void testZeroK() {
        int[] nums = {1, 2, 3, 4, 5};
        int k = 0;
        long[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, KRadiusSubArrayAverages.kRadiusAverages(nums, k));
    }

    @Test
    void testLargeK() {
        int[] nums = {1, 2, 3, 4, 5};
        int k = 2;
        long[] expected = {-1, -1, 3, -1, -1};
        assertArrayEquals(expected, KRadiusSubArrayAverages.kRadiusAverages(nums, k));
    }

    @Test
    void testHugeArray() {
        int n = 1_000_000;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i % 100;
        }
        int k = 500;
        long[] result = KRadiusSubArrayAverages.kRadiusAverages(nums, k);

        // Check the first and last k elements are -1
        for (int i = 0; i < k; i++) {
            assertEquals(-1, result[i]);
            assertEquals(-1, result[n - i - 1]);
        }

        // Spot check a valid average (no need to check all to keep the test fast)
        int sampleIndex = 600;
        long sum = 0;
        for (int i = sampleIndex - k; i <= sampleIndex + k; i++) {
            sum += nums[i];
        }
        int expected = (int)(sum / (2 * k + 1));
        assertEquals(expected, result[sampleIndex]);
    }
}
