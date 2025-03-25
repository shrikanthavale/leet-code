package com.shrikane.arrays.twopointers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubstringTest {

    @Test
    void testIsSubstring_ValidSubstring() {
        String subString = "sub";
        String originalString = "this is a substring";
        
        assertTrue(Substring.isSubstring(subString, originalString),
                   "The method should return true for a valid substring.");
    }

    @Test
    void testIsSubstring_InvalidSubstring() {
        String subString = "not";
        String originalString = "this is a substring";
        
        assertFalse(Substring.isSubstring(subString, originalString), 
                    "The method should return false for an invalid substring.");
    }

    @Test
    void testIsSubstring_EmptySubstring() {
        String subString = "";
        String originalString = "this is a substring";
        
        assertTrue(Substring.isSubstring(subString, originalString), 
                   "The method should return true when the substring is empty (always a substring).");
    }

    @Test
    void testIsSubstring_EmptyOriginalString() {
        String subString = "sub";
        String originalString = "";
        
        assertFalse(Substring.isSubstring(subString, originalString), 
                    "The method should return false if the original string is empty.");
    }

    @Test
    void testIsSubstring_SubstringEqualToOriginalString() {
        String subString = "substring";
        String originalString = "substring";
        
        assertTrue(Substring.isSubstring(subString, originalString), 
                   "The method should return true if the substring is the same as the original string.");
    }

    @Test
    void testIsSubstring_SubstringAtStart() {
        String subString = "this";
        String originalString = "this is a substring";
        
        assertTrue(Substring.isSubstring(subString, originalString), 
                   "The method should return true if the substring occurs at the start of the original string.");
    }

    @Test
    void testIsSubstring_SubstringAtEnd() {
        String subString = "string";
        String originalString = "this is a substring";
        
        assertTrue(Substring.isSubstring(subString, originalString), 
                   "The method should return true if the substring occurs at the end of the original string.");
    }

    @Test
    void testIsSubstring_SubstringInMiddle() {
        String subString = "is a";
        String originalString = "this is a substring";
        
        assertTrue(Substring.isSubstring(subString, originalString), 
                   "The method should return true if the substring occurs in the middle of the original string.");
    }

    @Test
    void testIsSubstring_SpecialCharacters() {
        String subString = "!@#";
        String originalString = "this is a substring with special characters !@# inside";
        
        assertTrue(Substring.isSubstring(subString, originalString), 
                   "The method should handle special characters and return true for valid substrings.");
    }

    @Test
    void testIsSubstring_CaseSensitive() {
        String subString = "Substring";
        String originalString = "this is a substring";
        
        assertFalse(Substring.isSubstring(subString, originalString), 
                    "The method should be case-sensitive and return false if the substring differs in case.");
    }

    @Test
    void testIsSubstring_SubstringNonConsecutive() {
        String subString = "isn";
        String originalString = "this is a substring";
        
        assertTrue(Substring.isSubstring(subString, originalString),
                    "The method should return true if the characters in the substring are not consecutive in the original string.");
    }
}