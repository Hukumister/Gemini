package com.haroncode.gemini.binder.coordinator

import com.haroncode.gemini.binder.rule.BindingRule
import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class Coordinator(
    private val coroutineScope: CoroutineScope,
    private val rule: BindingRule,
) : Consumer<StoreLifecycleEvent> {

    override fun accept(value: StoreLifecycleEvent) {
        when (value) {
            StoreLifecycleEvent.ON_ATTACH -> coroutineScope.launch { rule.bind() }
            StoreLifecycleEvent.ON_DETACH -> coroutineScope.coroutineContext.cancelChildren()
        }
    }
}
