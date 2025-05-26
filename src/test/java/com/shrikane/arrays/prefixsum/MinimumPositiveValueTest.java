package com.shrikane.arrays.prefixsum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MinimumPositiveValueTest {

    @Test
    void testExample1() {
        int[] nums = {-3, 2, -3, 4, 2};
        assertEquals(5, MinimumPositiveValue.minimumPositiveValue(nums));
    }

    @Test
    void testAllPositive() {
        int[] nums = {1, 2, 3};
        assertEquals(1, MinimumPositiveValue.minimumPositiveValue(nums));
    }

    @Test
    void testAllNegative() {
        int[] nums = {-1, -2, -3};
        assertEquals(7, MinimumPositiveValue.minimumPositiveValue(nums));
    }

    @Test
    void testMixedValues() {
        int[] nums = {1, -2, -3, 4};
        assertEquals(5, MinimumPositiveValue.minimumPositiveValue(nums));
    }

    @Test
    void testEmptyArray() {
        int[] nums = {};
        assertEquals(1, MinimumPositiveValue.minimumPositiveValue(nums));
    }
}
