package com.github.soumikbhatt.plugintranslators.utils

import java.util.regex.Pattern

object PlaceholderUtils {
    // Regex for Android string placeholders like %s, %1$s, %1$.2f, etc.
    private val PLACEHOLDER_PATTERN = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])")

    /**
     * Wraps placeholders in <span class="notranslate"> tags so Google Translate ignores them.
     */
    fun protectPlaceholders(text: String): String {
        val matcher = PLACEHOLDER_PATTERN.matcher(text)
        val sb = StringBuilder()
        var lastEnd = 0
        while (matcher.find()) {
            sb.append(text.substring(lastEnd, matcher.start()))
            sb.append("<span class=\"notranslate\">")
            sb.append(matcher.group())
            sb.append("</span>")
            lastEnd = matcher.end()
        }
        sb.append(text.substring(lastEnd))
        return sb.toString()
    }

    /**
     * Removes the protection tags after translation.
     */
    fun unprotectPlaceholders(text: String): String {
        return text.replace("<span class=\"notranslate\">", "")
            .replace("<span class = \"notranslate\">", "") // Handle potential spacing from API
            .replace("</span>", "")
            .replace(" </ span>", "")
            .replace("</ span>", "")
            .replace("</span >", "")
    }
}
