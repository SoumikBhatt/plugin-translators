package com.github.soumikbhatt.plugintranslators.actions

import com.github.soumikbhatt.plugintranslators.services.SettingsService
import com.github.soumikbhatt.plugintranslators.services.TranslationTask
import com.github.soumikbhatt.plugintranslators.ui.LanguageSelectionDialog
import com.github.soumikbhatt.plugintranslators.utils.NotificationUtils
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class TranslateStringsAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val isStringsXml = virtualFile?.name == "strings.xml"
        e.presentation.isEnabledAndVisible = isStringsXml
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        // 1. Check if configured
        val settings = SettingsService.getInstance(project)
        if (settings.selectedProvider == "google" && settings.googleTranslateApiKey.isBlank()) {
            NotificationUtils.showError(project, "Please configure your Google Translate API Key in Settings -> Tools -> Android String Translator")
            return
        }
        if (settings.selectedProvider == "libre" && settings.libreTranslateUrl.isBlank()) {
            NotificationUtils.showError(project, "Please configure your LibreTranslate URL in Settings -> Tools -> Android String Translator")
            return
        }

        // 2. Show Language Selection Dialog
        val dialog = LanguageSelectionDialog()
        if (dialog.showAndGet()) {
            val targetLanguage = dialog.getSelectedLanguage() ?: return
            
            // 3. Start Background Task
            TranslationTask(
                project,
                virtualFile,
                targetLanguage
            ).queue()
        }
    }
}
