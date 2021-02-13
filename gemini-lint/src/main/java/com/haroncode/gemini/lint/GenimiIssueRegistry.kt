package com.haroncode.gemini.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class GenimiIssueRegistry : IssueRegistry() {
    override val issues = WrongGenimiUsageDetector.issues

    override val api = CURRENT_API
}
