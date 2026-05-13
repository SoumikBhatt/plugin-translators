package com.github.soumikbhatt.plugintranslators.services

import com.github.soumikbhatt.plugintranslators.utils.PlaceholderUtils
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LibreTranslateProvider(
    private val baseUrl: String = "https://libretranslate.de",
    private val apiKey: String = ""
) : TranslatorProvider {

    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    @Serializable
    data class LibreRequest(
        val q: List<String>,
        val source: String = "auto",
        val target: String,
        val format: String = "html",
        val api_key: String = ""
    )

    @Serializable
    data class LibreResponse(
        val translatedText: JsonElement // Can be string or array
    )

    override fun translate(texts: List<String>, targetLanguageCode: String): List<String> {
        if (texts.isEmpty()) return emptyList()

        val protectedTexts = texts.map { PlaceholderUtils.protectPlaceholders(it) }

        val requestBody = LibreRequest(
            q = protectedTexts,
            target = targetLanguageCode,
            api_key = apiKey
        )

        val request = Request.Builder()
            .url("${baseUrl.trimEnd('/')}/translate")
            .header("Accept", "application/json")
            .header("User-Agent", "Mozilla/5.0")
            .post(json.encodeToString(LibreRequest.serializer(), requestBody).toRequestBody(mediaType))
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string() ?: throw IOException("Empty response body")

            if (!response.isSuccessful) {
                throw IOException("LibreTranslate API error: ${response.code} - $body")
            }

            val element = try {
                json.parseToJsonElement(body)
            } catch (e: Exception) {
                throw IOException("LibreTranslate returned a non-JSON response (likely an HTML error page). Please check your URL and if the instance is available. Response snippet: ${body.take(200)}")
            }
            
            // LibreTranslate can return either { "translatedText": "..." } for single string
            // or { "translatedText": ["...", "..."] } for array q.
            // Or sometimes it returns just the array of strings directly depending on the instance version.
            
            return when (element) {
                is JsonObject -> {
                    val translatedText = element["translatedText"]
                    when (translatedText) {
                        is JsonArray -> translatedText.map { PlaceholderUtils.unprotectPlaceholders(it.jsonPrimitive.content) }
                        is JsonPrimitive -> listOf(PlaceholderUtils.unprotectPlaceholders(translatedText.content))
                        else -> throw IOException("Unexpected translatedText format in LibreTranslate response")
                    }
                }
                is JsonArray -> {
                    element.map { PlaceholderUtils.unprotectPlaceholders(it.jsonPrimitive.content) }
                }
                else -> throw IOException("Unexpected LibreTranslate response format")
            }
        }
    }

    override fun getProviderId(): String = "libre"
}
