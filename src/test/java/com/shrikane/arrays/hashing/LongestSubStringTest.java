package com.shrikane.arrays.hashing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class LongestSubStringTest {

    @Test
    void testBasic() {
        assertEquals(3, LongestSubString.longestSubStringWithAtmostKDistinctCharacters("eceba", 2));
    }

    @Test
    void testAllSameCharacters() {
        assertEquals(5, LongestSubString.longestSubStringWithAtmostKDistinctCharacters("aaaaa", 1));
    }

    @Test
    void testAllUniqueCharacters() {
        assertEquals(4, LongestSubString.longestSubStringWithAtmostKDistinctCharacters("abcd", 4));
    }

    @Test
    void testKGreaterThanDistinctChars() {
        assertEquals(6, LongestSubString.longestSubStringWithAtmostKDistinctCharacters("aabbcc", 10));
    }

    @Test
    void testZeroK() {
        assertEquals(0, LongestSubString.longestSubStringWithAtmostKDistinctCharacters("anything", 0));
    }

    @Test
    void testEmptyString() {
        assertEquals(0, LongestSubString.longestSubStringWithAtmostKDistinctCharacters("", 2));
    }
}
