package com.shrikane.arrays.hashing;

import java.util.HashMap;

/**
 * Given an array of integers nums and an integer target, return indices of two numbers such that they add up to target.
 * You cannot use the same index twice.
 */
public class TwoSum {

    private TwoSum() {
        // private constructor to avoid initialization
    }

    public static int[] twoSum(int[] nums, int target) {
        final HashMap<Integer, Integer> elementIndexMap = new HashMap<>();

        for (int index = 0; index < nums.length; index++) {
            elementIndexMap.putIfAbsent(nums[index], index);

            int expectedElement = target - nums[index];
            Integer indexFound = elementIndexMap.get(expectedElement);
            if (indexFound != null && indexFound != index) {
                return new int[] {indexFound, index};
            }
        }

        return new int[0];
    }
}
