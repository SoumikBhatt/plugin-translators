package com.github.soumikbhatt.plugintranslators.parser

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.psi.xml.XmlFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Assert.assertEquals

class StringResourceParserTest : BasePlatformTestCase() {

    fun `test parse extracts string resources correctly`() {
        val xmlContent = """
            <resources>
                <string name="app_name">My App</string>
                <string name="greeting">Hello %s!</string>
                <!-- This is a comment -->
                <string name="farewell">Goodbye</string>
            </resources>
        """.trimIndent()

        val psiFile = myFixture.configureByText(XmlFileType.INSTANCE, xmlContent)
        val xmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val resources = StringResourceParser.parse(xmlFile)

        assertEquals(3, resources.size)
        
        assertEquals("app_name", resources[0].name)
        assertEquals("My App", resources[0].value)

        assertEquals("greeting", resources[1].name)
        assertEquals("Hello %s!", resources[1].value)

        assertEquals("farewell", resources[2].name)
        assertEquals("Goodbye", resources[2].value)
    }

    fun `test parse returns empty list for invalid root tag`() {
        val xmlContent = """
            <manifest>
                <application name="My App" />
            </manifest>
        """.trimIndent()

        val psiFile = myFixture.configureByText(XmlFileType.INSTANCE, xmlContent)
        val xmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val resources = StringResourceParser.parse(xmlFile)

        assertEquals(0, resources.size)
    }
}
