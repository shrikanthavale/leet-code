package com.shrikane.arrays.slidingwindow;

public class MaximumConsecutiveOnes {

    private MaximumConsecutiveOnes() {
        // cannot initialize
    }

    public static int longestSubstringAfterFlip(String stringOfZerosAndOne) {
        int left = 0;
        int currentLength = 0;
        int zerosAllowed = 0;
        int maxLength = 0;

        for (int right = 0; right < stringOfZerosAndOne.length(); right++) {
            if (stringOfZerosAndOne.charAt(right) == '0') {
                zerosAllowed = zerosAllowed + 1;
            }

            while (zerosAllowed > 1) {
                left = left + 1;
                if (stringOfZerosAndOne.charAt(left) == '0') {
                    zerosAllowed = zerosAllowed - 1;
                }
                currentLength = 0;
            }

            currentLength = currentLength + 1;
            maxLength = Math.max(maxLength, currentLength);
        }

        return maxLength;
    }
}
