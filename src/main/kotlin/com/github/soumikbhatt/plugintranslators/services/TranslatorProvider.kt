package com.github.soumikbhatt.plugintranslators.services

/**
 * Interface for different translation API providers.
 */
interface TranslatorProvider {
    /**
     * Translates a list of texts to the target language.
     * @param texts List of raw string values to translate.
     * @param targetLanguageCode Target ISO language code (e.g., "bn", "hi").
     * @return List of translated string values.
     */
    fun translate(texts: List<String>, targetLanguageCode: String): List<String>
    
    /**
     * Unique ID for the provider to be stored in settings.
     */
    fun getProviderId(): String
}
