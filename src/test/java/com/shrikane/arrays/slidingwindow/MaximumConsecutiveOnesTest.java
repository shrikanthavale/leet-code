package com.shrikane.arrays.slidingwindow;

import static com.shrikane.arrays.slidingwindow.MaximumConsecutiveOnes.longestSubstringAfterFlip;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MaximumConsecutiveOnesTest {

    @Test
    void testLongestSubstringAfterFlip() {
        assertEquals(3, longestSubstringAfterFlip("110"));
        assertEquals(4, longestSubstringAfterFlip("11010"));
        assertEquals(1, longestSubstringAfterFlip("0"));
        assertEquals(3, longestSubstringAfterFlip("1010"));
        assertEquals(6, longestSubstringAfterFlip("111011"));
        assertEquals(7, longestSubstringAfterFlip("1111011"));
        assertEquals(2, longestSubstringAfterFlip("10001"));
        assertEquals(1, longestSubstringAfterFlip("00000"));
    }
}
