package com.shrikane.arrays.hashing;

import java.util.HashMap;
import java.util.Map;

/**
 * You are given a string s and an integer k. Find the length of the longest substring that contains at most
 * k distinct characters.
 */
public class LongestSubString {

    private LongestSubString() {

    }

    /**
     * You are given a string s and an integer k. Find the length of the longest substring that contains at
     * most k distinct characters.
     *
     * @param s input string.
     * @param k at most distinct.
     * @return longest sub string.
     */
    public static int longestSubStringWithAtmostKDistinctCharacters(final String s, final int k) {

        if (s == null || s.isEmpty()) {
            return 0;
        }

        if (k == 0) {
            return k;
        }

        int leftIndex = 0;
        int longestSubStringLength = 0;

        final Map<Character, Integer> characterIntegerMap = HashMap.newHashMap(s.length());

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            characterIntegerMap.put(c, characterIntegerMap.getOrDefault(c, 0));


            while (characterIntegerMap.size() > k && leftIndex < s.length()) {
                char charToRemove = s.charAt(leftIndex);
                characterIntegerMap.computeIfPresent(charToRemove, (key, value) ->
                    value - 1 == 0 ? null : value - 1);
                leftIndex++;
            }

            longestSubStringLength = Math.max(longestSubStringLength, right - leftIndex + 1);
        }

        return longestSubStringLength;

    }
}
