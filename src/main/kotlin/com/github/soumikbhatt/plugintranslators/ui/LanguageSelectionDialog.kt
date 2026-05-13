package com.github.soumikbhatt.plugintranslators.ui

import com.github.soumikbhatt.plugintranslators.models.Language
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ComboBox
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class LanguageSelectionDialog : DialogWrapper(true) {

    private val languageComboBox = ComboBox(Language.SUPPORTED_LANGUAGES.toTypedArray())

    init {
        title = "Select Target Language"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout(0, 10))
        panel.add(JLabel("Choose the language to translate to:"), BorderLayout.NORTH)
        panel.add(languageComboBox, BorderLayout.CENTER)
        return panel
    }

    fun getSelectedLanguage(): Language? = languageComboBox.selectedItem as? Language
}
