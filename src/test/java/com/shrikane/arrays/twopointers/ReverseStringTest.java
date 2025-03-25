package com.shrikane.arrays.twopointers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReverseStringTest {

    @Test
    void testReverseString_NormalInput() {
        char[] input = {'h', 'e', 'l', 'l', 'o'};
        char[] expected = {'o', 'l', 'l', 'e', 'h'};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should correctly reverse a normal array of characters.");
    }

    @Test
    void testReverseString_SingleCharacter() {
        char[] input = {'a'};
        char[] expected = {'a'};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should return the same array for a single character.");
    }

    @Test
    void testReverseString_EmptyArray() {
        char[] input = {};
        char[] expected = {};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should return an empty array when the input is empty.");
    }

    @Test
    void testReverseString_SpecialCharacters() {
        char[] input = {'@', '#', '$', '%', '^'};
        char[] expected = {'^', '%', '$', '#', '@'};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should correctly reverse an array of special characters.");
    }

    @Test
    void testReverseString_NumericCharacters() {
        char[] input = {'1', '2', '3', '4', '5'};
        char[] expected = {'5', '4', '3', '2', '1'};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should correctly reverse an array of numeric characters.");
    }

    @Test
    void testReverseString_Palindrome() {
        char[] input = {'r', 'a', 'c', 'e', 'c', 'a', 'r'};
        char[] expected = {'r', 'a', 'c', 'e', 'c', 'a', 'r'};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should not modify arrays that are palindromes.");
    }

    @Test
    void testReverseString_MixedCase() {
        char[] input = {'A', 'b', 'C', 'd', 'E'};
        char[] expected = {'E', 'd', 'C', 'b', 'A'};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should correctly reverse arrays with mixed case letters.");
    }

    @Test
    void testReverseString_UnicodeCharacters() {
        char[] input = {'α', 'β', 'γ', 'δ', 'ε'};
        char[] expected = {'ε', 'δ', 'γ', 'β', 'α'};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should correctly reverse arrays with Unicode characters.");
    }

    @Test
    void testReverseString_Whitespace() {
        char[] input = {' ', 'a', 'b', ' ', 'c', ' '};
        char[] expected = {' ', 'c', ' ', 'b', 'a', ' '};

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should correctly handle arrays containing whitespace.");
    }

    @Test
    void testReverseString_LongArray() {
        char[] input = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] expected = "zyxwvutsrqponmlkjihgfedcba".toCharArray();

        char[] result = ReverseString.reverseString(input);

        assertArrayEquals(expected, result, "The method should correctly reverse long arrays.");
    }
}