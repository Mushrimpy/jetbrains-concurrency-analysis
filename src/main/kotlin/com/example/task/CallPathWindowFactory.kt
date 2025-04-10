package com.example.task

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.JComponent


class CallPathWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentPanel = createWelcomePanel()
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(
            contentPanel,
            "Welcome",
            false
        )
        toolWindow.contentManager.addContent(content)
    }


    private fun createWelcomePanel(): JComponent {
        val panel = JBPanel<JBPanel<*>>(BorderLayout())

        val welcomeText = """
            <html>
            <h2>Call Path Analysis</h2>
            <p>This tool helps you analyze method call paths in your Java code.</p>
            <br>
            <p><b>How to use:</b></p>
            <ol>
                <li>Navigate to a Java method in your editor</li>
                <li>Place your cursor on a method name</li>
                <li>Press Alt+Enter or Option+Enter </li>
                <li>Select "Find Call Paths" from the menu</li>
                <li>Enter the target method name when prompted</li>
            </ol>
            <br>
            <p>The call path analysis results will appear in this window.</p>
            </html>
        """.trimIndent()

        val label = JBLabel(welcomeText)
        panel.add(JBScrollPane(label))

        return panel
    }

}


