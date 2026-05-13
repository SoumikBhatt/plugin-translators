package com.github.soumikbhatt.plugintranslators.generator

import com.github.soumikbhatt.plugintranslators.models.StringResource
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager

object ResourceFileGenerator {

    /**
     * Generates a translated strings.xml file in the appropriate values-xx directory.
     */
    fun generate(
        project: Project,
        baseDir: VirtualFile, // Typically the 'res' directory
        languageCode: String,
        resources: List<StringResource>
    ) {
        val targetDirName = if (languageCode == "en") "values" else "values-$languageCode"
        
        WriteCommandAction.runWriteCommandAction(project) {
            // 1. Create or get target directory
            val targetDir = baseDir.findChild(targetDirName) ?: baseDir.createChildDirectory(this, targetDirName)
            
            // 2. Create or get strings.xml
            val stringsFile = targetDir.findChild("strings.xml") ?: targetDir.createChildData(this, "strings.xml")
            
            // 3. Generate XML content
            val xmlContent = buildXmlContent(resources)
            
            // 4. Write content to file
            stringsFile.setBinaryContent(xmlContent.toByteArray(Charsets.UTF_8))
            
            // 5. Refresh PSI to reflect changes in editor
            val psiFile = PsiManager.getInstance(project).findFile(stringsFile)
            if (psiFile != null) {
                PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(
                    PsiDocumentManager.getInstance(project).getDocument(psiFile)!!
                )
            }
        }
    }

    private fun buildXmlContent(resources: List<StringResource>): String {
        val sb = StringBuilder()
        sb.append("<resources>\n")
        resources.forEach { res ->
            // Simple escape for XML special characters in values
            val escapedValue = res.value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
            
            sb.append("    <string name=\"${res.name}\">$escapedValue</string>\n")
        }
        sb.append("</resources>")
        return sb.toString()
    }
}
