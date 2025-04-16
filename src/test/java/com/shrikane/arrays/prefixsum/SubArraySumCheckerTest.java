package com.shrikane.arrays.prefixsum;

import static com.shrikane.arrays.prefixsum.SubArraySumChecker.checkSubArraySum;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

class SubArraySumCheckerTest {

    @Test
     void testcheckSubArraySum_Basic() {
        int[] nums = {1, 2, 3, 4, 5};
        int[][] queries = {{0, 2}, {1, 3}, {0, 4}};
        int limit = 10;

        boolean[] expected = {false, false, true};
        boolean[] actual = checkSubArraySum(nums, queries, limit);

        assertArrayEquals(expected, actual);
    }

    @Test
     void testEmptyArray() {
        int[] nums = {};
        int[][] queries = {};
        int limit = 5;

        boolean[] expected = {};
        boolean[] actual = checkSubArraySum(nums, queries, limit);

        assertArrayEquals(expected, actual);
    }

    @Test
     void testSingleElement() {
        int[] nums = {7};
        int[][] queries = {{0, 0}};
        int limit = 10;

        boolean[] expected = {false};
        boolean[] actual = checkSubArraySum(nums, queries, limit);

        assertArrayEquals(expected, actual);
    }

    @Test
     void testSameElements() {
        int[] nums = {5, 5, 5, 5};
        int[][] queries = {{0, 1}, {1, 3}, {0, 3}};
        int limit = 15;

        boolean[] expected = {false, false, true}; // 10, 15, 20
        boolean[] actual = checkSubArraySum(nums, queries, limit);

        assertArrayEquals(expected, actual);
    }

    @Test
     void testNegativeNumbers() {
        int[] nums = {-2, -3, 4, -1, -2, 1, 5, -3};
        int[][] queries = {{0, 2}, {2, 4}, {0, 7}};
        int limit = 0;

        boolean[] expected = {false, true, false}; // -1, 1, -1
        boolean[] actual = checkSubArraySum(nums, queries, limit);

        assertArrayEquals(expected, actual);
    }

    @Test
     void testLargeRange() {
        int[] nums = new int[1000];
        for (int i = 0; i < 1000; i++) {
            nums[i] = 1;
        }
        int[][] queries = {{0, 999}};
        int limit = 1001;

        boolean[] expected = {false}; // sum is 1000
        boolean[] actual = checkSubArraySum(nums, queries, limit);

        assertArrayEquals(expected, actual);
    }
}
