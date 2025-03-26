package com.shrikane.arrays.slidingwindow;

import static com.shrikane.arrays.slidingwindow.LongestSubArrayWithSum.lengthOfLongestSubArray;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class LongestSubArrayWithSumTest {

    @Test
    void testBasicCases() {
        assertEquals(3, lengthOfLongestSubArray(new int[] {1, 2, 3, 4, 5}, 7));
        assertEquals(4, lengthOfLongestSubArray(new int[] {1, 1, 1, 1, 1, 1}, 4));
        assertEquals(3, lengthOfLongestSubArray(new int[] {3, 1, 2, 1, 4, 5}, 5));
    }

    @Test
    void testEdgeCases() {
        assertEquals(0, lengthOfLongestSubArray(new int[] {5, 6, 7}, 4)); // No valid subarray
        assertEquals(5, lengthOfLongestSubArray(new int[] {1, 2, 1, 2, 1}, 10)); // Whole array
        assertEquals(1, lengthOfLongestSubArray(new int[] {10, 20, 30}, 15)); // Single element case
    }

    @Test
    void testEmptyArray() {
        assertEquals(0, lengthOfLongestSubArray(new int[] {}, 10)); // Empty array case
    }
}
