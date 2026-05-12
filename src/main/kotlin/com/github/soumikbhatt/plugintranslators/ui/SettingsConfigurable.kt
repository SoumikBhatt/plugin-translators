package com.github.soumikbhatt.plugintranslators.ui

import com.github.soumikbhatt.plugintranslators.services.SettingsService
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class SettingsConfigurable(private val project: Project) : Configurable {

    private var mySettingsComponent: JPanel? = null
    private val providerComboBox = ComboBox(arrayOf("Google Translate", "LibreTranslate"))
    private val googleApiKeyField = JBPasswordField()
    private val libreUrlField = JBTextField()
    private val libreApiKeyField = JBPasswordField()

    override fun getDisplayName(): String = "Android String Translator"

    override fun createComponent(): JComponent {
        providerComboBox.addActionListener {
            updateVisibility()
        }

        mySettingsComponent = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Translation Provider: "), providerComboBox, 1, false)
            .addSeparator()
            .addLabeledComponent(JBLabel("Google API Key: "), googleApiKeyField, 1, false)
            .addLabeledComponent(JBLabel("LibreTranslate URL: "), libreUrlField, 1, false)
            .addLabeledComponent(JBLabel("LibreTranslate API Key (Optional): "), libreApiKeyField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
            
        updateVisibility()
        return mySettingsComponent!!
    }

    private fun updateVisibility() {
        val isGoogle = providerComboBox.selectedIndex == 0
        googleApiKeyField.isVisible = isGoogle
        googleApiKeyField.parent?.getComponent(googleApiKeyField.parent.getComponentZOrder(googleApiKeyField) - 1)?.isVisible = isGoogle
        
        libreUrlField.isVisible = !isGoogle
        libreUrlField.parent?.getComponent(libreUrlField.parent.getComponentZOrder(libreUrlField) - 1)?.isVisible = !isGoogle
        libreApiKeyField.isVisible = !isGoogle
        libreApiKeyField.parent?.getComponent(libreApiKeyField.parent.getComponentZOrder(libreApiKeyField) - 1)?.isVisible = !isGoogle
    }

    override fun isModified(): Boolean {
        val settings = SettingsService.getInstance(project)
        return providerComboBox.selectedIndex != (if (settings.selectedProvider == "google") 0 else 1) ||
                String(googleApiKeyField.password) != settings.googleTranslateApiKey ||
                libreUrlField.text != settings.libreTranslateUrl ||
                String(libreApiKeyField.password) != settings.libreTranslateApiKey
    }

    override fun apply() {
        val settings = SettingsService.getInstance(project)
        settings.selectedProvider = if (providerComboBox.selectedIndex == 0) "google" else "libre"
        settings.googleTranslateApiKey = String(googleApiKeyField.password)
        settings.libreTranslateUrl = libreUrlField.text
        settings.libreTranslateApiKey = String(libreApiKeyField.password)
    }

    override fun reset() {
        val settings = SettingsService.getInstance(project)
        providerComboBox.selectedIndex = if (settings.selectedProvider == "google") 0 else 1
        googleApiKeyField.text = settings.googleTranslateApiKey
        libreUrlField.text = settings.libreTranslateUrl
        libreApiKeyField.text = settings.libreTranslateApiKey
        updateVisibility()
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
