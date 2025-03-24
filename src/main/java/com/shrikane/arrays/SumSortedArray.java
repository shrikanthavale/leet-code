package com.shrikane.arrays;

import java.util.Arrays;

/**
 * The SumSortedArray class provides a method to determine if there are two numbers
 * in a sorted array that sum up to a given target value.
 */
public class SumSortedArray {

    /**
     * Determines if there are two numbers in a sorted array that add up to a given target value.
     * This method uses a two-pointer approach to find the pair of numbers.
     *
     * @param nums a sorted array of integers
     * @param target the target sum to check for
     * @return true if any two numbers in the array add up to the target, false otherwise
     */
    public boolean targetMatchesSum(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                return true;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return false;
    }
}
