package com.shrikane.arrays.slidingwindow;

/**
 * Find the length of longest sub array whose sum is less than or equal to given sum
 */
public class LongestSubArrayWithSum {

    private LongestSubArrayWithSum() {
        // avoid initialization
    }

    /**
     * Find the length of longest sub array whose sum is less than given sum.
     *
     * @param givenArray original array.
     * @param sum        should be less than this sum.
     * @return length of the longest array.
     */
    public static int lengthOfLongestSubArray(int[] givenArray, int sum) {

        int left = 0;
        int currentSum = 0;
        int length = 0;

        for (int right = 0; right < givenArray.length; right++) {

            currentSum = currentSum + givenArray[right];
            while (currentSum > sum) {
                currentSum = currentSum - givenArray[left];
                left++;
            }

            length = Math.max(length, right - left + 1);
        }

        return length;
    }
}
