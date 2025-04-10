package com.example.task

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

// Intention action that allows users to find execution paths to a specified method.
class CallPathIntention : PsiElementBaseIntentionAction() {

    override fun getText(): String = "Find method call paths"
    override fun getFamilyName(): String = "Method call path analysis"


    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return PsiTreeUtil.getParentOfType(element, PsiMethod::class.java) != null
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val sourceMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java) ?: return

        val targetMethodName = Messages.showInputDialog(
            project,
            "Enter target method name",
            "Find Method Call Paths",
            null
        ) ?: return
        if (targetMethodName.isEmpty())
            return

        val tracer = CallPathTracer()
        val paths = tracer.findCallPaths(sourceMethod, targetMethodName)
        val resultText = tracer.formatCallPaths(paths)

        CallPathService.getInstance(project).showResultsInConsole(
            resultText,
            sourceMethod.name,
            targetMethodName
        )
    }

    override fun startInWriteAction(): Boolean = false
}