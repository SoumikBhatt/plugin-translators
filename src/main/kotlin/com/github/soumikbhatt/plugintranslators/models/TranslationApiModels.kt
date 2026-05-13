package com.github.soumikbhatt.plugintranslators.models

import kotlinx.serialization.Serializable

@Serializable
data class TranslationRequest(
    val q: List<String>,
    val target: String,
    val format: String = "text"
)

@Serializable
data class TranslationResponse(
    val data: TranslationData
)

@Serializable
data class TranslationData(
    val translations: List<TranslationResult>
)

@Serializable
data class TranslationResult(
    val translatedText: String
)
