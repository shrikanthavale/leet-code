package com.shrikane.arrays.twopointers;

/**
 * The Palindrome class provides a method to check if a given string is a palindrome.
 * A palindrome is a string that reads the same backward as forward.
 */
public class Palindrome {

    /**
     * Checks if the given string is a palindrome. A palindrome is a string that reads
     * the same backward as forward.
     *
     * @param stringToCheck the string to be checked for palindrome property
     * @return true if the string is a palindrome, false otherwise
     */
    public boolean isPalindrome(String stringToCheck) {
        final int left = 0;
        final int right = stringToCheck.length() - 1;
        for (int i = left; i <= right; i++) {
            if (stringToCheck.charAt(i) != stringToCheck.charAt(right - i)) {
                return false;
            }
        }
        return true;
    }
}
