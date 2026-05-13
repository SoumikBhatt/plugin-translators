package com.github.soumikbhatt.plugintranslators.models

/**
 * Represents a single string resource from strings.xml
 * Example: <string name="hello">Hello</string>
 * @property name The name attribute of the string tag
 * @property value The text content of the string tag
 */
data class StringResource(
    val name: String,
    val value: String
)
