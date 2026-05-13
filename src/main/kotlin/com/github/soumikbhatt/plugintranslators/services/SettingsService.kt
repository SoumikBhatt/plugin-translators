package com.github.soumikbhatt.plugintranslators.services

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

/**
 * Persists the plugin settings (API key) across sessions.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "com.github.soumikbhatt.plugintranslators.Settings",
    storages = [Storage("AndroidStringTranslatorSettings.xml")]
)
class SettingsService : PersistentStateComponent<SettingsService.State> {

    class State {
        var googleTranslateApiKey: String = ""
        var selectedProvider: String = "google" // Default to libre for now as requested
        var libreTranslateUrl: String = "https://libretranslate.de"
        var libreTranslateApiKey: String = ""
    }

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState = state
    }

    var googleTranslateApiKey: String
        get() = myState.googleTranslateApiKey
        set(value) {
            myState.googleTranslateApiKey = value
        }

    var selectedProvider: String
        get() = myState.selectedProvider
        set(value) {
            myState.selectedProvider = value
        }

    var libreTranslateUrl: String
        get() = myState.libreTranslateUrl
        set(value) {
            myState.libreTranslateUrl = value
        }

    var libreTranslateApiKey: String
        get() = myState.libreTranslateApiKey
        set(value) {
            myState.libreTranslateApiKey = value
        }

    companion object {
        fun getInstance(project: Project): SettingsService = project.service()
    }
}
