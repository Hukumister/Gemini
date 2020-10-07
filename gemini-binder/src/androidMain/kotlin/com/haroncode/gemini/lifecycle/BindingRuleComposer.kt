package com.haroncode.gemini.lifecycle

import com.haroncode.gemini.binder.rule.BindingRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class BindingRuleComposer(
    private val coroutineScope: CoroutineScope,
    private val rules: Collection<BindingRule>
) : BindingRule {

    override suspend fun bind() = rules.forEach { rule -> coroutineScope.launch { rule.bind() } }
}
