package com.shrikane.arrays.slidingwindow;

import static com.shrikane.arrays.slidingwindow.SubArrayProductLessThanK.countSubArraysWhoseProductIsLessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SubArrayProductTest {

    @Test
    void testNumSubarrayProductLessThanK() {
        assertEquals(8, countSubArraysWhoseProductIsLessThan(new int[] {10, 5, 2, 6}, 100));
        assertEquals(0, countSubArraysWhoseProductIsLessThan(new int[] {1, 2, 3}, 0));
        assertEquals(0, countSubArraysWhoseProductIsLessThan(new int[] {1, 2, 3}, 1));
        assertEquals(6, countSubArraysWhoseProductIsLessThan(new int[] {1, 1, 1}, 2));
        assertEquals(7, countSubArraysWhoseProductIsLessThan(new int[] {1, 2, 3, 4}, 10));
    }
}
