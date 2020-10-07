package com.haroncode.gemini.binder.rule

import com.haroncode.gemini.element.Store
import kotlinx.coroutines.cancel

class AutoCancelStoreRule(
    private val store: Store<*, *, *>
) : BindingRule {

    override suspend fun bind() = Unit

    fun cancel() = store.coroutineScope.cancel()
}
