package com.shrikane.arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Palindrome
 */
class PalindromeTest {

    private final Palindrome palindrome = new Palindrome();

    @Test
    void testIsPalindrome_withPalindromeString() {
        assertTrue(palindrome.isPalindrome("madam"), "The string 'madam' is a palindrome.");
        assertTrue(palindrome.isPalindrome("racecar"), "The string 'racecar' is a palindrome.");
        assertTrue(palindrome.isPalindrome("a"), "A single character is always a palindrome.");
        assertTrue(palindrome.isPalindrome(""), "An empty string is always a palindrome.");
    }

    @Test
    void testIsPalindrome_withNonPalindromeString() {
        assertFalse(palindrome.isPalindrome("hello"), "The string 'hello' is not a palindrome.");
        assertFalse(palindrome.isPalindrome("world"), "The string 'world' is not a palindrome.");
        assertFalse(palindrome.isPalindrome("palindrome"), "The string 'palindrome' is not a palindrome.");
    }

    @Test
    void testIsPalindrome_withMixedCaseStrings() {
        assertFalse(palindrome.isPalindrome("Madam"), "The check is case-sensitive. 'Madam' is not considered a palindrome.");
        assertFalse(palindrome.isPalindrome("RaceCar"), "The check is case-sensitive. 'RaceCar' is not considered a palindrome.");
    }

    @Test
    void testIsPalindrome_withSpecialCharacters() {
        assertFalse(palindrome.isPalindrome("madam!"), "The string 'madam!' is not a palindrome due to the special character.");
        assertFalse(palindrome.isPalindrome("!madam"), "The string '!madam' is not a palindrome due to the added special character.");
    }
}