package com.shrikane.arrays;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MergeTwoSortedArraysTest {

    @Test
    void testMergeTwoSortedArrays_BothArraysNonEmpty() {
        int[] array1 = {1, 3, 5};
        int[] array2 = {2, 4, 6};
        int[] expected = {1, 2, 3, 4, 5, 6};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The merged array should be sorted.");
    }

    @Test
    void testMergeTwoSortedArrays_FirstArrayEmpty() {
        int[] array1 = {};
        int[] array2 = {1, 2, 3};
        int[] expected = {1, 2, 3};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The result should match the non-empty second array.");
    }

    @Test
    void testMergeTwoSortedArrays_SecondArrayEmpty() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {};
        int[] expected = {1, 2, 3};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The result should match the non-empty first array.");
    }

    @Test
    void testMergeTwoSortedArrays_BothArraysEmpty() {
        int[] array1 = {};
        int[] array2 = {};
        int[] expected = {};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The result should be an empty array.");
    }

    @Test
    void testMergeTwoSortedArrays_SameElementsInBothArrays() {
        int[] array1 = {1, 1, 1};
        int[] array2 = {1, 1, 1};
        int[] expected = {1, 1, 1, 1, 1, 1};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The result should consist of all duplicates sorted.");
    }

    @Test
    void testMergeTwoSortedArrays_DifferentSizes() {
        int[] array1 = {1, 2};
        int[] array2 = {3, 4, 5, 6};
        int[] expected = {1, 2, 3, 4, 5, 6};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The result should contain all elements sorted.");
    }

    @Test
    void testMergeTwoSortedArrays_NegativeValues() {
        int[] array1 = {-5, -3, -1};
        int[] array2 = {-4, -2, 0};
        int[] expected = {-5, -4, -3, -2, -1, 0};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The result should handle negative numbers correctly.");
    }

    @Test
    void testMergeTwoSortedArrays_MixedEmptyAndSingleElement() {
        int[] array1 = {5};
        int[] array2 = {};
        int[] expected = {5};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The result should handle cases with single element arrays.");
    }

    @Test
    void testMergeTwoSortedArrays_OneArrayWithRepeatingValues() {
        int[] array1 = {1, 1, 1, 1};
        int[] array2 = {2, 2};
        int[] expected = {1, 1, 1, 1, 2, 2};

        int[] result = MergeTwoSortedArrays.mergeTwoSortedArrays(array1, array2);

        assertArrayEquals(expected, result, "The method should handle arrays with repeated values.");
    }
}