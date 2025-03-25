package com.shrikane.arrays.twopointers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

class SquareSortedArrayTest {

    @Test
    void testSortedSquares_AllPositiveNumbers() {
        int[] input = {1, 2, 3, 4};
        int[] expected = {1, 4, 9, 16};

        SquareSortedArray squareSortedArray = new SquareSortedArray();
        int[] result = squareSortedArray.sortedSquares(input);

        assertArrayEquals(expected, result, "The method should correctly handle an array of all positive numbers.");
    }

    @Test
    void testSortedSquares_AllNegativeNumbers() {
        int[] input = {-4, -3, -2, -1};
        int[] expected = {1, 4, 9, 16};

        SquareSortedArray squareSortedArray = new SquareSortedArray();
        int[] result = squareSortedArray.sortedSquares(input);

        assertArrayEquals(expected, result, "The method should correctly handle an array of all negative numbers.");
    }

    @Test
    void testSortedSquares_MixedPositiveAndNegativeNumbers() {
        int[] input = {-4, -2, 0, 1, 3};
        int[] expected = {0, 1, 4, 9, 16};

        SquareSortedArray squareSortedArray = new SquareSortedArray();
        int[] result = squareSortedArray.sortedSquares(input);

        assertArrayEquals(expected, result,
            "The method should correctly handle a mix of positive and negative numbers.");
    }

    @Test
    void testSortedSquares_ContainsZero() {
        int[] input = {-2, -1, 0, 1, 2};
        int[] expected = {0, 1, 1, 4, 4};

        SquareSortedArray squareSortedArray = new SquareSortedArray();
        int[] result = squareSortedArray.sortedSquares(input);

        assertArrayEquals(expected, result, "The method should correctly handle input with zero.");
    }

    @Test
    void testSortedSquares_SingleElement() {
        int[] input = {-3};
        int[] expected = {9};

        SquareSortedArray squareSortedArray = new SquareSortedArray();
        int[] result = squareSortedArray.sortedSquares(input);

        assertArrayEquals(expected, result, "The method should correctly handle input with a single element.");
    }

    @Test
    void testSortedSquares_AllZeroes() {
        int[] input = {0, 0, 0};
        int[] expected = {0, 0, 0};

        SquareSortedArray squareSortedArray = new SquareSortedArray();
        int[] result = squareSortedArray.sortedSquares(input);

        assertArrayEquals(expected, result, "The method should correctly handle input with all zeroes.");
    }

    @Test
    void testSortedSquares_LargeArray() {
        int[] input = {-10000, -5000, -200, 0, 10, 5000, 10000};
        int[] expected = {0, 100, 40000, 25000000, 25000000, 100000000, 100000000};

        SquareSortedArray squareSortedArray = new SquareSortedArray();
        int[] result = squareSortedArray.sortedSquares(input);

        assertArrayEquals(expected, result, "The method should correctly handle a large input array.");
    }

    @Test
    void testSortedSquares_DescendingInput() {
        // Although this violates the assumption of sorted input, it tests method robustness.
        int[] input = {4, 3, 2, 1};
        int[] expected = {1, 4, 9, 16};

        SquareSortedArray squareSortedArray = new SquareSortedArray();
        int[] result = squareSortedArray.sortedSquares(input);

        assertArrayEquals(expected, result,
            "The method should not depend on manually unsorted input (clarify assumption).");
    }
}