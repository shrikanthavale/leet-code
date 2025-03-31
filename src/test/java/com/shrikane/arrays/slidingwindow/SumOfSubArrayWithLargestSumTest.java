package com.shrikane.arrays.slidingwindow;

import static com.shrikane.arrays.slidingwindow.SumOfSubArrayWithLargestSum.sumOfSubArrayWithLargestSumHavingLength;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SumOfSubArrayWithLargestSumTest {

    @Test
    void testMaxSumSubarrayOfSizeK() {
        assertEquals(9, sumOfSubArrayWithLargestSumHavingLength(new int[] {2, 1, 5, 1, 3, 2}, 3));
        assertEquals(7, sumOfSubArrayWithLargestSumHavingLength(new int[] {2, 3, 4, 1, 5}, 2));
        assertEquals(5, sumOfSubArrayWithLargestSumHavingLength(new int[] {1, 2, 3}, 2));
        assertEquals(0, sumOfSubArrayWithLargestSumHavingLength(new int[] {1, 2}, 3));
        assertEquals(15, sumOfSubArrayWithLargestSumHavingLength(new int[] {1, 2, 3, 4, 5}, 5));
    }
}
