package com.shrikane.arrays.twopointers;

/**
 * The ReverseString class provides a method to reverse the characters of a given array of characters.
 * Reversing is achieved by swapping characters from the two ends of the array towards the center.
 */
public class ReverseString {

    private ReverseString() {
        // Prevent instantiation
    }

    /**
     * Reverses the characters in the given character array.
     * The input array is modified in place by swapping characters from the two ends of the array
     * towards the center.
     *
     * @param stringToReverse the array of characters to be reversed
     * @return the reversed array of characters
     */
    public static char[] reverseString(char[] stringToReverse) {
        int left = 0;
        int right = stringToReverse.length - 1;
        char temp;

        while (left < right) {
            temp = stringToReverse[left];
            stringToReverse[left] = stringToReverse[right];
            stringToReverse[right] = temp;
            left++;
            right--;
        }

        return stringToReverse;
    }
}
