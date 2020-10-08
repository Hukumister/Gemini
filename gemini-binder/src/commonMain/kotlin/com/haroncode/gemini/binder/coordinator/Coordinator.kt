package com.haroncode.gemini.binder.coordinator

import com.haroncode.gemini.binder.rule.BindingRule
import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class Coordinator(
    uiCoroutineDispatcher: CoroutineDispatcher,
    private val rules: Collection<BindingRule>,
) : Consumer<StoreLifecycleEvent> {

    private val coroutineScope = CoroutineScope(SupervisorJob() + uiCoroutineDispatcher)

    override fun accept(value: StoreLifecycleEvent) {
        when (value) {
            StoreLifecycleEvent.ON_ATTACH -> rules.forEach { rule ->
                coroutineScope.launch { rule.bind() }
            }
            StoreLifecycleEvent.ON_DETACH -> coroutineScope.coroutineContext.cancelChildren()
        }
    }
}
