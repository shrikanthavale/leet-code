package com.shrikane.arrays.hashing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TwoSumTest {

    @Test
    void testTwoSumFound() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = TwoSum.twoSum(nums, target);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    void testNoSolution() {
        int[] nums = {1, 2, 3};
        int target = 7;
        int[] result = TwoSum.twoSum(nums, target);

        assertEquals(0, result.length);
    }

    @Test
    void testWithNegativeNumbers() {
        int[] nums = {-3, 4, 3, 90};
        int target = 0;
        int[] result = TwoSum.twoSum(nums, target);
        assertNotNull(result);
        assertArrayEquals(new int[] {0, 2}, result);
    }

    @Test
    void testWithDuplicates() {
        int[] nums = {3, 3};
        int target = 6;
        int[] result = TwoSum.twoSum(nums, target);
        assertNotNull(result);
        assertArrayEquals(new int[] {0, 1}, result);
    }

    @Test
    void testWithZeros() {
        int[] nums = {0, 4, 3, 0};
        int target = 0;
        int[] result = TwoSum.twoSum(nums, target);
        assertNotNull(result);
        assertArrayEquals(new int[] {0, 3}, result);
    }

    @Test
    void testWithLargeNumbers() {
        int[] nums = {1000000, 2000000, 3000000, 5000000};
        int target = 8000000;
        int[] result = TwoSum.twoSum(nums, target);
        assertNotNull(result);
        assertArrayEquals(new int[] {2, 3}, result);
    }

    @Test
    void testSingleElementArray() {
        int[] nums = {1};
        int target = 1;
        int[] result = TwoSum.twoSum(nums, target);
        assertEquals(0, result.length);
    }

    @Test
    void testEmptyArray() {
        int[] nums = {};
        int target = 0;
        int[] result = TwoSum.twoSum(nums, target);
        assertEquals(0, result.length);
    }
}
