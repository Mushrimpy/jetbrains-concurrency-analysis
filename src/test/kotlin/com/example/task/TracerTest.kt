package com.example.task

import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TracerTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "testdata"

    private lateinit var tracer: CallPathTracer

    override fun setUp() {
        super.setUp()
        tracer = CallPathTracer()
    }

    fun testNoPath() {
        val paths = findPathsFromFile("NoPath.java", "A", "B")
        assertTrue(paths.isEmpty())
    }

    fun testDirectCall() {
        val paths = findPathsFromFile("DirectCall.java", "A", "B")
        assertEquals(1, paths.size)
        assertEquals(listOf("A", "B"), paths[0])
    }

    fun testChainCall() {
        val paths = findPathsFromFile("ChainCall.java", "A", "C")
        assertEquals(1, paths.size)
        assertEquals(listOf("A", "B", "C"), paths[0])
    }

    fun testBranchingCall() {
        val paths = findPathsFromFile("BranchingCall.java", "source", "target")
        val expectedPaths = setOf(
            listOf("source", "A", "target"),
            listOf("source", "B", "target")
        )
        assertEquals(2, paths.size)
        assertTrue(expectedPaths.contains(paths[0]))
        assertTrue(expectedPaths.contains(paths[1]))
    }

    fun testRecursiveCall() {
        val paths = findPathsFromFile("RecursiveCall.java", "A", "C")
        // Despite recursion in B, we still expect the path A -> B -> C
        assertEquals(1, paths.size)
        assertEquals(listOf("A", "B", "C"), paths[0])
    }

    private fun findPathsFromFile(
        fileName: String,
        sourceMethodName: String,
        targetMethodName: String
    ): List<List<String>> {
        val psiFile = myFixture.configureByFile(fileName)
        val classFile = (psiFile as PsiJavaFile).classes.first()
        val sourceMethod = classFile.findMethodsByName(sourceMethodName, false)[0]
        return tracer.findCallPaths(sourceMethod, targetMethodName)
    }
}