package com.shrikane.arrays.slidingwindow;

import static com.shrikane.arrays.slidingwindow.MaxConsecutiveOnesWithAllowedFlips.longestOnes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MaxConsecutiveOnesWithAllowedFlipsTest {

    @Test
    void testExample1() {
        int[] nums = {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0};
        int k = 2;
        assertEquals(6, longestOnes(nums, k));
    }

    @Test
    void testExample2() {
        int[] nums = {0, 0, 1, 1, 1, 0, 0};
        int k = 0;
        assertEquals(3, longestOnes(nums, k));
    }

    @Test
    void testAllOnes() {
        int[] nums = {1, 1, 1, 1, 1};
        int k = 1;
        assertEquals(5, longestOnes(nums, k));
    }

    @Test
    void testAllZeros() {
        int[] nums = {0, 0, 0, 0};
        int k = 2;
        assertEquals(2, longestOnes(nums, k));
    }

    @Test
    void testSingleElement() {
        int[] nums = {0};
        int k = 1;
        assertEquals(1, longestOnes(nums, k));
    }
}
