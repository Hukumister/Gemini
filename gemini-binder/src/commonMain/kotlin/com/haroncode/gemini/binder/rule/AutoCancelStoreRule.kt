package com.haroncode.gemini.binder.rule

import com.haroncode.gemini.element.Store
import kotlinx.coroutines.cancel

@Deprecated(
    message = "Will be removed in upcoming release, have to use getStore extension to retain and cancel store instance in ViewModel",
    replaceWith = ReplaceWith("getStore(provider)", imports = ["com.haroncode.gemini.keeper.getStore"])
)
class AutoCancelStoreRule(
    private val store: Store<*, *, *>
) : BindingRule {

    override suspend fun bind() = Unit

    fun cancel() = store.coroutineScope.cancel()
}
