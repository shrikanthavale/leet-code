package com.shrikane.arrays.slidingwindow;

public class SumOfSubArrayWithLargestSum {

    private SumOfSubArrayWithLargestSum() {
        // private constructor
    }

    public static int sumOfSubArrayWithLargestSumHavingLength(int [] arrayToInvestigate, int maxLengthOfSubArray) {
        int left = 0;
        int right = 0;
        int sum = 0;
        int maxSum = 0;

        for (;right < arrayToInvestigate.length; right++) {
            sum = sum + arrayToInvestigate[right];

            if (right - left + 1 == maxLengthOfSubArray) {
                maxSum = Math.max(maxSum, sum);

                sum = sum - arrayToInvestigate[left];
                left++;
            }
        }

        return maxSum;
    }
}
