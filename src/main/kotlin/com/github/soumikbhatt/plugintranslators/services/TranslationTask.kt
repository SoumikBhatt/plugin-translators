package com.github.soumikbhatt.plugintranslators.services

import com.github.soumikbhatt.plugintranslators.generator.ResourceFileGenerator
import com.github.soumikbhatt.plugintranslators.models.Language
import com.github.soumikbhatt.plugintranslators.models.StringResource
import com.github.soumikbhatt.plugintranslators.parser.StringResourceParser
import com.github.soumikbhatt.plugintranslators.utils.NotificationUtils
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

class TranslationTask(
    project: Project,
    private val sourceFile: VirtualFile,
    private val targetLanguage: Language
) : Task.Backgroundable(project, "Translating Strings to ${targetLanguage.name}...", true) {

    override fun run(indicator: ProgressIndicator) {
        try {
            indicator.isIndeterminate = false
            
            // 1. Parse source strings
            indicator.text = "Parsing strings.xml..."
            indicator.fraction = 0.1
            val psiFile = PsiManager.getInstance(project).findFile(sourceFile) ?: throw IllegalStateException("Could not find PSI for file")
            val originalResources = StringResourceParser.parse(psiFile)
            
            if (originalResources.isEmpty()) {
                NotificationUtils.showError(project, "No strings found to translate.")
                return
            }

            // 2. Translate
            indicator.text = "Translating ${originalResources.size} strings..."
            indicator.fraction = 0.3
            
            val settings = SettingsService.getInstance(project)
            val provider: TranslatorProvider = if (settings.selectedProvider == "google") {
                GoogleTranslateProvider(settings.googleTranslateApiKey)
            } else {
                LibreTranslateProvider(settings.libreTranslateUrl, settings.libreTranslateApiKey)
            }
            
            val translatedValues = provider.translate(
                originalResources.map { it.value },
                targetLanguage.code
            )

            // 3. Match translated values back to names
            indicator.text = "Generating translated file..."
            indicator.fraction = 0.8
            val translatedResources = originalResources.mapIndexed { index, original ->
                StringResource(original.name, translatedValues[index])
            }

            // 4. Generate file
            val resDir = sourceFile.parent?.parent ?: throw IllegalStateException("Could not find 'res' directory")
            ResourceFileGenerator.generate(
                project,
                resDir,
                targetLanguage.code,
                translatedResources
            )

            indicator.fraction = 1.0
            NotificationUtils.showSuccess(project, "Successfully translated ${originalResources.size} strings to ${targetLanguage.name}")

        } catch (e: Exception) {
            NotificationUtils.showError(project, "Translation failed: ${e.message}")
            e.printStackTrace()
        }
    }
}
