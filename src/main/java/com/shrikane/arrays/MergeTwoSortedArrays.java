package com.shrikane.arrays;

/**
 * The MergeTwoSortedArrays class provides a method to merge two sorted arrays into a single sorted array.
 * This ensures that the resulting array maintains the sorted order.
 */
public class MergeTwoSortedArrays {

    private MergeTwoSortedArrays() {
        // Prevent instantiation
    }

    /**
     * Merges two sorted arrays into a single sorted array.
     * The method combines both input arrays while maintaining the sorted order.
     *
     * @param array1 the first input array, which is sorted in ascending order
     * @param array2 the second input array, which is also sorted in ascending order
     * @return a new array containing all elements from array1 and array2, sorted in ascending order
     */
    public static int[] mergeTwoSortedArrays(final int[] array1, final int[] array2) {

        final int[] result = new int[array1.length + array2.length];
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < array1.length && j < array2.length) {
            if (array1[i] < array2[j]) {
                result[k] = array1[i];
                k++;
                i++;
            } else if (array1[i] > array2[j]) {
                result[k] = array2[j];
                k++;
                j++;
            } else {
                result[k] = array1[i];
                k++;
                i++;
                result[k] = array2[j];
                k++;
                j++;
            }
        }

        while (i < array1.length) {
            result[k] = array1[i];
            k++;
            i++;
        }

        while (j < array2.length) {
            result[k] = array2[j];
            k++;
            j++;
        }

        return result;
    }
}
