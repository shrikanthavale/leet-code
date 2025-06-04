package com.shrikane.arrays.hashing;

import java.util.HashSet;
import java.util.Set;

/**
 * Given a string s, return the first character to appear twice. It is guaranteed that the input will have a
 * duplicate character.
 */
public class FirstLetterToAppearTwice {

    private FirstLetterToAppearTwice() {
        // to avoid external initialization
    }

    public static char firstLetterToAppearTwice(String stringToCheck) {
        if (stringToCheck == null || stringToCheck.isEmpty()) {
            return ' ';
        }

        final Set<Character> characters = HashSet.newHashSet(stringToCheck.length());

        for (int index = 0; index < stringToCheck.length(); index++) {
            final char c = stringToCheck.charAt(index);
            boolean add = characters.add(c);
            if (!add) {
                return c;
            }
        }

        return ' ';
    }
}
