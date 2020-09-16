package com.haroncode.gemini.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.whenStarted
import com.haroncode.gemini.binder.BaseBindingRule
import kotlinx.coroutines.launch

/**
 * @author HaronCode
 * @author kdk96
 */
object StartStopStrategy : LifecycleStrategy {

    override fun bind(
        lifecycleOwner: LifecycleOwner,
        rules: Collection<BaseBindingRule<*, *>>
    ) {
        with(lifecycleOwner) {
            lifecycle.coroutineScope.launch {
                rules.forEach { rule ->
                    launch { whenStarted { rule.bind() } }
                }
            }
        }
    }
}
