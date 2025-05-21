package com.shrikane.arrays.prefixsum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SplitArrayTest {

    @Test
    void testAllPositiveNumbers() {
        int[] arr = {10, 20, 30, 40};
        assertEquals(1, SplitArray.numberOfWaysToSplitArray(arr), "Test failed for all positive numbers");
    }

    @Test
    void testAllNegativeNumbers() {
        int[] arr = {-10, -20, -30, -40};
        assertEquals(2, SplitArray.numberOfWaysToSplitArray(arr), "Test failed for all negative numbers");
    }

    @Test
    void testMixedPositiveAndNegativeNumbers() {
        int[] arr = {10, -5, 5, 10};
        assertEquals(2, SplitArray.numberOfWaysToSplitArray(arr),
            "Test failed for mixed positive and negative numbers");
    }

    @Test
    void testSingleElementArray() {
        int[] arr = {10};
        assertEquals(0, SplitArray.numberOfWaysToSplitArray(arr), "Test failed for single element array");
    }

    @Test
    void testTwoElementArray() {
        int[] arr = {10, 20};
        assertEquals(0, SplitArray.numberOfWaysToSplitArray(arr), "Test failed for two elements array");
    }

    @Test
    void testEqualElementsArray() {
        int[] arr = {5, 5, 5, 5};
        assertEquals(2, SplitArray.numberOfWaysToSplitArray(arr), "Test failed for array with equal elements");
    }

    @Test
    void testLongArray() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals(2, SplitArray.numberOfWaysToSplitArray(arr),
            "Test failed for large array with increasing numbers");
    }

    @Test
    void testLargeNumbers() {
        int[] arr = {Integer.MAX_VALUE, 0, 0, Integer.MAX_VALUE};
        assertEquals(3, SplitArray.numberOfWaysToSplitArray(arr), "Test failed for array with large numbers");
    }

    @Test
    void testEmptyArray() {
        int[] arr = {};
        assertEquals(0, SplitArray.numberOfWaysToSplitArray(arr), "Test failed for empty array");
    }

    @Test
    void testEdgeCaseBoundarySplit() {
        int[] arr = {1, 1, 1, 1, 1, 1};
        assertEquals(3, SplitArray.numberOfWaysToSplitArray(arr), "Test failed for boundary edge case");
    }


}
