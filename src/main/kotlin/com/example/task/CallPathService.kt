package com.example.task

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

@Service(Service.Level.PROJECT)
class CallPathService(private val project: Project) {

    companion object {
        private const val TOOL_WINDOW_ID = "Call Path Analysis"
        fun getInstance(project: Project): CallPathService {
            return project.getService(CallPathService::class.java)
        }
    }

    fun showResultsInConsole(resultText: String, sourceMethodName: String, targetMethodName: String) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TOOL_WINDOW_ID) ?: return

        val contentPanel = JPanel(BorderLayout())
        val textArea = JTextArea().apply {
            text = buildHeader(sourceMethodName, targetMethodName) + resultText
            isEditable = false
            caretPosition = 0
        }
        contentPanel.add(JScrollPane(textArea))

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(
            contentPanel,
            "$sourceMethodName → $targetMethodName",
            false
        )

        toolWindow.contentManager.addContent(content)
        toolWindow.show()
    }

    private fun buildHeader(sourceMethodName: String, targetMethodName: String): String {
        return "Source method: $sourceMethodName\nTarget method: $targetMethodName\n\n"
    }
}

