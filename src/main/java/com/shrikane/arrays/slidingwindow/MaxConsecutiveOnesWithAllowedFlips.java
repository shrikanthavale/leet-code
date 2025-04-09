package com.shrikane.arrays.slidingwindow;

/**
 * Given a binary array nums and an integer k, return the maximum number of consecutive 1's in the array if you can flip
 * at most k 0's.
 */
public class MaxConsecutiveOnesWithAllowedFlips {

    private MaxConsecutiveOnesWithAllowedFlips() {
        // no initialization
    }

    /**
     * Given a binary array nums and an integer allowedFlips, return the maximum number of consecutive 1's in the array
     * if you can flip at most k 0's.
     *
     * @param nums         binary array nums
     * @param allowedFlips an integer allowedFlips
     * @return maximum number of consecutive 1's
     */
    public static int longestOnes(int[] nums, int allowedFlips) {
        int left = 0;
        int right = 0;
        int flipsCount = 0;
        int onesCount = 0;
        int maxOnesCount = onesCount;

        for (; right < nums.length; right++) {
            if (nums[right] == 1) {
                onesCount = right - left + 1;
            } else if (nums[right] == 0) {
                flipsCount = flipsCount + 1;
                if (flipsCount <= allowedFlips) {
                    onesCount = right - left + 1;
                }
            }

            while (flipsCount > allowedFlips && left < nums.length) {
                maxOnesCount = Math.max(onesCount, maxOnesCount);

                if (nums[left] == 0) {
                    flipsCount = flipsCount - 1;
                }

                left = left + 1;
            }

        }

        return Math.max(onesCount, maxOnesCount);
    }
}
