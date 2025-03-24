package com.shrikane.arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SumSortedArray class.
 */
class SumSortedArrayTest {

    private final SumSortedArray sumSortedArray = new SumSortedArray();

    @Test
    void testTargetMatchesSum_withValidPairs() {
        // Test case with valid pairs adding up to the target
        assertTrue(sumSortedArray.targetMatchesSum(new int[] {1, 2, 3, 4, 5}, 6),
            "There are two numbers (2, 4) that sum up to 6.");
        assertTrue(sumSortedArray.targetMatchesSum(new int[] {1, 2, 3, 4, 5}, 9),
            "There are two numbers (4, 5) that sum up to 9.");
        assertTrue(sumSortedArray.targetMatchesSum(new int[] {0, 1, 2, 3, 4}, 4),
            "There are two numbers (1, 3) that sum up to 4.");
    }

    @Test
    void testTargetMatchesSum_withNoValidPairs() {
        // Test case where no pairs add up to the target
        assertFalse(sumSortedArray.targetMatchesSum(new int[] {1, 2, 3, 4, 5}, 10),
            "There are no two numbers that sum up to 10.");
        assertFalse(sumSortedArray.targetMatchesSum(new int[] {-3, -2, 0, 1, 2, 5}, 8),
            "There are no two numbers that sum up to 8.");
        assertFalse(sumSortedArray.targetMatchesSum(new int[] {1, 3, 5, 7, 9}, 4),
            "There are no two numbers that sum up to 4.");
    }

    @Test
    void testTargetMatchesSum_withEdgeCases() {
        // Test edge cases
        assertTrue(sumSortedArray.targetMatchesSum(new int[] {2, 2}, 4),
            "There are two numbers (2, 2) that sum up to 4.");
        assertFalse(sumSortedArray.targetMatchesSum(new int[] {}, 5),
            "An empty array cannot have pairs, so no matching sum.");
        assertFalse(sumSortedArray.targetMatchesSum(new int[] {1}, 1), "A single-element array cannot have pairs.");
    }

    @Test
    void testTargetMatchesSum_withNegativeNumbers() {
        // Test case with negative numbers
        assertTrue(sumSortedArray.targetMatchesSum(new int[] {-5, -3, -1, 0, 3, 6}, 0),
            "There are two numbers (-3, 3) that sum up to 0.");
        assertTrue(sumSortedArray.targetMatchesSum(new int[] {-10, -5, -2, 0, 1, 4}, -5),
            "There are two numbers (-5, 0) that sum up to -5.");
        assertFalse(sumSortedArray.targetMatchesSum(new int[] {-5, -3, 0, 3, 6}, 10),
            "There are no two numbers that sum up to 10.");
    }

    @Test
    void testTargetMatchesSum_withLargeArray() {
        // Test case with a large array
        int[] nums = new int[1000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i;
        }
        assertTrue(sumSortedArray.targetMatchesSum(nums, 1998),
            "The numbers 999 and 999 in the large array sum to 1998.");
        assertFalse(sumSortedArray.targetMatchesSum(nums, 2001), "No two numbers in the array sum to 2001.");
    }
}