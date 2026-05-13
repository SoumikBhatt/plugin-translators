package com.github.soumikbhatt.plugintranslators.parser

import com.github.soumikbhatt.plugintranslators.models.StringResource
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag

object StringResourceParser {

    /**
     * Parses a strings.xml file and extracts <string> tags.
     * @param psiFile The PSI file (should be an XmlFile)
     * @return List of StringResource models
     */
    fun parse(psiFile: PsiFile): List<StringResource> {
        if (psiFile !is XmlFile) return emptyList()

        val rootTag = psiFile.rootTag ?: return emptyList()
        if (rootTag.name != "resources") return emptyList()

        val resources = mutableListOf<StringResource>()
        
        rootTag.subTags.forEach { tag ->
            if (tag.name == "string") {
                val name = tag.getAttributeValue("name")
                val value = tag.value.text
                if (name != null) {
                    resources.add(StringResource(name, value))
                }
            }
        }

        return resources
    }
}
