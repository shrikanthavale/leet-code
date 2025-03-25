package com.shrikane.arrays.twopointers;

/**
 * The SquareSortedArray class provides a method to compute the squares of elements
 * in a sorted array and return a new array with the squares sorted in non-decreasing order.
 * This is achieved using a two-pointer approach.
 */
public class SquareSortedArray {

    /**
     * Computes the squares of each element in a sorted array and returns a new array
     * with these squared values sorted in non-decreasing order. The input array is assumed
     * to be sorted in non-decreasing order. A two-pointer approach is used to achieve an
     * O(n) time complexity.
     *
     * @param nums a sorted array of integers in non-decreasing order
     * @return an array of integers containing the squares of the input elements, sorted in non-decreasing order
     */
    public int[] sortedSquares(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int[] squares = new int[nums.length];
        int squaresIndex = nums.length - 1;

        while (left <= right) {
            int num1Square = nums[left] * nums[left];
            int num2Square = nums[right] * nums[right];

            if (num1Square < num2Square) {
                squares[squaresIndex] = num2Square;
                squaresIndex--;
                right--;
            } else {
                squares[squaresIndex] = num1Square;
                squaresIndex--;
                left++;
            }
        }

        return squares;
    }
}
