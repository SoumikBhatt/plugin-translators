package com.github.soumikbhatt.plugintranslators.services

import com.github.soumikbhatt.plugintranslators.models.TranslationRequest
import com.github.soumikbhatt.plugintranslators.models.TranslationResponse
import com.github.soumikbhatt.plugintranslators.utils.PlaceholderUtils
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException

class GoogleTranslateProvider(private val apiKey: String) : TranslatorProvider {

    private val client = OkHttpClient().newBuilder().addInterceptor {
        val request = it.request()
        val payloadBuffer = Buffer()
        request.body?.writeTo(payloadBuffer)
        val payload = payloadBuffer.readUtf8()

        println("""
            |------- REQUEST -------
            |URL     : ${request.url}
            |METHOD  : ${request.method}
            |HEADERS : ${request.headers}
            |PAYLOAD : $payload
            |------------------------
        """.trimMargin())

        val response = it.proceed(request)
        val contentType = response.body.contentType()
        val responseBody = response.body.string()

        println("""
            |------- RESPONSE -------
            |CODE    : ${response.code}
            |HEADERS : ${response.headers}
            |BODY    : $responseBody
            |------------------------
        """.trimMargin())

        response.newBuilder()
            .body(responseBody.toByteArray().toResponseBody(contentType))
            .build()
    }.build()
    private val json = Json { ignoreUnknownKeys = true }
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    override fun translate(texts: List<String>, targetLanguageCode: String): List<String> {
        if (apiKey.isBlank()) throw IllegalStateException("Google API Key is missing.")
        if (texts.isEmpty()) return emptyList()

        val protectedTexts = texts.map { PlaceholderUtils.protectPlaceholders(it) }

        val requestBody = TranslationRequest(
            q = protectedTexts,
            target = targetLanguageCode,
            format = "html"
        )

        val request = Request.Builder()
            .url("https://translation.googleapis.com/language/translate/v2?key=$apiKey")
            .post(json.encodeToString(TranslationRequest.serializer(), requestBody).toRequestBody(mediaType))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: "Unknown error"
                throw IOException("Google Translate API error: ${response.code} - $errorBody")
            }

            val body = response.body?.string() ?: throw IOException("Empty response body")
            val translationResponse = json.decodeFromString(TranslationResponse.serializer(), body)

            return translationResponse.data.translations.map { 
                PlaceholderUtils.unprotectPlaceholders(it.translatedText) 
            }
        }
    }

    override fun getProviderId(): String = "google"
}
