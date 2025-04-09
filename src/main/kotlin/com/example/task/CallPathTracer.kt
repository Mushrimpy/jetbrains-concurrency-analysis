package com.example.task

import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression

// Core logic for tracing method call paths to a target method.
class CallPathTracer {

    fun findCallPaths(sourceMethod: PsiMethod, targetMethodName: String): List<List<String>> {
        val allPaths = mutableListOf<List<String>>()
        val currentPath = mutableListOf<String>()
        findPathsDFS(sourceMethod, targetMethodName, currentPath, allPaths)
        return allPaths
    }

    // Recursive depth-first search to find call paths to the target method.
    private fun findPathsDFS(
        currentMethod: PsiMethod,
        targetMethodName: String,
        currentPath: MutableList<String>,
        allPaths: MutableList<List<String>>
    ) {
        currentPath.add(currentMethod.name)

        if (currentMethod.name == targetMethodName && currentPath.size > 1) {
            allPaths.add(ArrayList(currentPath))
            currentPath.removeAt(currentPath.size - 1)
            return
        }
        val visitor = object : JavaRecursiveElementVisitor() {
            override fun visitMethodCallExpression(call: PsiMethodCallExpression) {
                super.visitMethodCallExpression(call)

                val resolvedMethod = call.resolveMethod()
                if (resolvedMethod is PsiMethod && !currentPath.contains(resolvedMethod.name)) {
                    findPathsDFS(resolvedMethod, targetMethodName, currentPath, allPaths)
                }
            }
        }
        currentMethod.accept(visitor)
        currentPath.removeAt(currentPath.size - 1)
    }

    fun formatCallPaths(paths: List<List<String>>): String {
        if (paths.isEmpty()) {
            return "No call paths found to the target method."
        }
        return buildString {
            append("Found ${paths.size} call path(s):\n")
            paths.forEachIndexed { index, path ->
                append("Path ${index + 1}: ${path.joinToString(" -> ")}\n")
            }
        }
    }
}