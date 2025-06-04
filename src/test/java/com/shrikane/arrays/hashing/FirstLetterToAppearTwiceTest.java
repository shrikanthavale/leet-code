package com.shrikane.arrays.hashing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class FirstLetterToAppearTwiceTest {

    @Test
    void testExample1() {
        assertEquals('a', FirstLetterToAppearTwice.firstLetterToAppearTwice("abca"));
    }

    @Test
    void testExample2() {
        assertEquals('a', FirstLetterToAppearTwice.firstLetterToAppearTwice("alphabetical"));
    }

    @Test
    void testExample3() {
        assertEquals('b', FirstLetterToAppearTwice.firstLetterToAppearTwice("bacb"));
    }

    @Test
    void testImmediateDuplicate() {
        assertEquals('x', FirstLetterToAppearTwice.firstLetterToAppearTwice("xxy"));
    }

    @Test
    void testLongString() {
        assertEquals('q', FirstLetterToAppearTwice.firstLetterToAppearTwice("abcdefghijklmnopqrstuvwxyzq"));
    }
}
