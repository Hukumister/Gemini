package com.haroncode.gemini.binder

import com.haroncode.gemini.StoreViewDelegate
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class BinderImpl<Action : Any, State : Any>(
    private val factoryProvider: () -> BindingRulesFactory<StoreViewDelegate<Action, State>>,
) : Binder<StoreViewDelegate<Action, State>> {

    override fun bind(view: StoreViewDelegate<Action, State>) {
        val bindingRules = factoryProvider().create(view)
        val coroutineScope = view.coroutineScope
        val baseBindingRules = bindingRules.filterIsInstance<BaseBindingRule<*, *>>()

        view.lifecycleObserver = object : StoreViewDelegate.LifecycleObserver {

            private var bindingJob: Job? = null

            override fun start() {
                bindingJob = coroutineScope.launch {
                    baseBindingRules.forEach { rule -> launch { rule.bind() } }
                }
            }

            override fun stop() {
                bindingJob?.cancel()
                bindingJob = null
            }

            override fun destroy() {
                view.lifecycleObserver = null
                bindingRules.filterIsInstance<AutoCancelStoreRule>()
                    .forEach(AutoCancelStoreRule::cancel)
            }
        }
    }
}
