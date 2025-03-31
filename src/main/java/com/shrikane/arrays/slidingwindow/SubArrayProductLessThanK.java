package com.shrikane.arrays.slidingwindow;

public class SubArrayProductLessThanK {

    private SubArrayProductLessThanK() {
        // to avoid external initialization
    }

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
