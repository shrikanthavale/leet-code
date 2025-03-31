package com.shrikane.arrays.slidingwindow;

/**
 * Given an array of positive integers nums and an integer k, return the number of subarrays where the product of all
 * the elements in the subarray is strictly less than k.
 */
public class SubArrayProductLessThanK {

    private SubArrayProductLessThanK() {
        // to avoid external initialization
    }

    /**
     * Given an array of positive integers nums and an integer k, return the number of subarrays where the product of
     * all the elements in the subarray is strictly less than k.
     *
     * @param arrayToInvestigate array to investigate.
     * @param k                  product is less than
     * @return number of subarrays.
     */
    public static int countSubArraysWhoseProductIsLessThan(int[] arrayToInvestigate, int k) {
        int left = 0;
        int right = 0;
        int currentProduct = 1;
        int subArrayCount = 0;

        for (; right < arrayToInvestigate.length; right++) {
            currentProduct = currentProduct * arrayToInvestigate[right];

            while (currentProduct >= k && left <= right) {
                currentProduct = currentProduct / arrayToInvestigate[left];
                left++;
            }

            subArrayCount = subArrayCount + (right - left + 1);

        }

        return subArrayCount;

    }
}
