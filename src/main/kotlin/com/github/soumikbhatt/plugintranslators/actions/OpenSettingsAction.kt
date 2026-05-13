package com.github.soumikbhatt.plugintranslators.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.github.soumikbhatt.plugintranslators.ui.SettingsConfigurable

class OpenSettingsAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        ShowSettingsUtil.getInstance().showSettingsDialog(project, SettingsConfigurable::class.java)
    }
}
