package com.shrikane.arrays.twopointers;

/**
 * The Substring class provides a method to check if one string is a substring of another string.
 * A substring is defined as a sequence of characters that appear consecutively in the given string.
 */
public class Substring {

    private Substring() {
        // Prevent instantiation
    }

    /**
     * Checks if a given string is a substring of another string.
     * A substring is defined as a sequence of characters that appear consecutively
     * in the given original string in the same order as they appear in the smaller string.
     *
     * @param subStringToCheck the string to check whether it is a substring
     * @param originalString the string in which to search for the substring
     * @return true if the subStringToCheck is a substring of originalString, false otherwise
     */
    public static boolean isSubstring(final String subStringToCheck, final String originalString) {

        int i = 0;
        int j = 0;

        if (subStringToCheck.length() > originalString.length()) {
            return false;
        }

        if (subStringToCheck.isEmpty()) {
            return true;
        }

        while (j < originalString.length()) {
            if (originalString.charAt(j) == subStringToCheck.charAt(i)) {
                i++;
                j++;
            } else {
                j++;
            }

            if (i == subStringToCheck.length()-1) {
                return true;
            }
        }

        return false;
    }
}
