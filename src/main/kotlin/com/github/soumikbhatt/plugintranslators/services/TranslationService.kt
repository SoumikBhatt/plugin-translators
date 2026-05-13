
package com.github.soumikbhatt.plugintranslators.services

import com.github.soumikbhatt.plugintranslators.models.TranslationRequest
import com.github.soumikbhatt.plugintranslators.models.TranslationResponse
import com.github.soumikbhatt.plugintranslators.utils.PlaceholderUtils
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class TranslationService(private val apiKey: String) {

    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    /**
     * Translates a list of string values to the target language.
     * @param texts List of raw string values to translate.
     * @param targetLanguageCode Target ISO language code (e.g., "bn", "hi").
     * @return List of translated string values.
     */
    fun translate(texts: List<String>, targetLanguageCode: String): List<String> {
        if (apiKey.isBlank()) throw IllegalStateException("API Key is missing. Please configure it in Settings.")
        if (texts.isEmpty()) return emptyList()

        // Protect placeholders
        val protectedTexts = texts.map { PlaceholderUtils.protectPlaceholders(it) }

        val requestBody = TranslationRequest(
            q = protectedTexts,
            target = targetLanguageCode,
            format = "html" // Use HTML to respect <span class="notranslate">
        )

        val request = Request.Builder()
            .url("https://translation.googleapis.com/language/translate/v2?key=$apiKey")
            .post(json.encodeToString(TranslationRequest.serializer(), requestBody).toRequestBody(mediaType))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: "Unknown error"
                throw IOException("Translation API error: ${response.code} - $errorBody")
            }

            val body = response.body?.string() ?: throw IOException("Empty response body")
            val translationResponse = json.decodeFromString(TranslationResponse.serializer(), body)

            return translationResponse.data.translations.map { 
                PlaceholderUtils.unprotectPlaceholders(it.translatedText) 
            }
        }
    }
}
