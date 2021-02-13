@file:Suppress("UnstableApiUsage")

package com.haroncode.gemini.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class WrongGenimiUsageDetector : Detector(), SourceCodeScanner {
    override fun getApplicableMethodNames() = listOf("bind")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val evaluator = context.evaluator

        if (evaluator.isMemberInSubClassOf(method, "com.haroncode.gemini.binder.Binder")) {
            checkCallMethod(method, node, context)
        }
    }



    private fun checkCallMethod(method: PsiMethod, call: UCallExpression, context: JavaContext) {
        var parent = call.uastParent
        while (parent != null && parent !is PsiMethod) {
            parent = parent.uastParent
        }
        if (parent !is PsiMethod) return
        if (parent.name != "onCreate") {
            context.report(ISSUE_ON_CREATE, method, context.getLocation(call), "Calling bind from ${parent.name} instead of onCreate")
        }
    }

    companion object {
        val ISSUE_ON_CREATE = Issue.create(
            "ShouldBeCalledInOnCreate",
            "Bind method for ViewStoreBinding should be called in onCreate method",
            "Based on documentation you should call bind in the onCreate method of your's view",
            Category.CORRECTNESS,
            5,
            Severity.ERROR,
            Implementation(WrongGenimiUsageDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val issues = listOf(
            ISSUE_ON_CREATE
        )
    }

}
