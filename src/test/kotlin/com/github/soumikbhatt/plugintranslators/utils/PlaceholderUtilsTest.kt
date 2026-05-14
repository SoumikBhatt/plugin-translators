package com.github.soumikbhatt.plugintranslators.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class PlaceholderUtilsTest {

    @Test
    fun `test protectPlaceholders string placeholder`() {
        val input = "Hello %s!"
        val expected = "Hello <span class=\"notranslate\">%s</span>!"
        val actual = PlaceholderUtils.protectPlaceholders(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `test protectPlaceholders positional string placeholder`() {
        val input = "Hello %1\$s!"
        val expected = "Hello <span class=\"notranslate\">%1\$s</span>!"
        val actual = PlaceholderUtils.protectPlaceholders(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `test protectPlaceholders digit placeholder`() {
        val input = "You have %d new messages"
        val expected = "You have <span class=\"notranslate\">%d</span> new messages"
        val actual = PlaceholderUtils.protectPlaceholders(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `test protectPlaceholders float placeholder`() {
        val input = "Price: %1\$.2f"
        val expected = "Price: <span class=\"notranslate\">%1\$.2f</span>"
        val actual = PlaceholderUtils.protectPlaceholders(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `test unprotectPlaceholders normal span`() {
        val input = "Bonjour <span class=\"notranslate\">%s</span>!"
        val expected = "Bonjour %s!"
        val actual = PlaceholderUtils.unprotectPlaceholders(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `test unprotectPlaceholders malformed span`() {
        val input = "Bonjour <span class = \"notranslate\">%s</ span>!"
        val expected = "Bonjour %s!"
        val actual = PlaceholderUtils.unprotectPlaceholders(input)
        assertEquals(expected, actual)
    }
}
