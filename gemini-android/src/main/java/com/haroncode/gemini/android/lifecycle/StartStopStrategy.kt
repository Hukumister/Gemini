package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.whenStarted
import com.haroncode.gemini.connector.BaseConnectionRule
import kotlinx.coroutines.launch

/**
 * @author HaronCode
 * @author kdk96
 */
object StartStopStrategy : LifecycleStrategy {

    override fun connect(
        lifecycleOwner: LifecycleOwner,
        rules: Collection<BaseConnectionRule<*, *>>
    ) {
        with(lifecycleOwner) {
            lifecycle.coroutineScope.launch {
                rules.forEach { rule ->
                    launch { whenStarted { rule.connect() } }
                }
            }
        }
    }
}
