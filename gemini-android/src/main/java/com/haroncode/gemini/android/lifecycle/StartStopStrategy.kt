package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.whenStarted
import com.haroncode.gemini.connector.ConnectionRule
import kotlinx.coroutines.launch

/**
 * @author HaronCode
 * @author kdk96
 */
object StartStopStrategy : LifecycleStrategy {

    override fun connect(
        lifecycleOwner: LifecycleOwner,
        rule: ConnectionRule
    ) {
        with(lifecycleOwner) {
            lifecycle.coroutineScope.launch {
                launch { whenStarted { rule.connect() } }
            }
        }
    }
}
