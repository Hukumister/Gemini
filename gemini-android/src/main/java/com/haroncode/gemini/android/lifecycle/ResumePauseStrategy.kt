package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.whenResumed
import com.haroncode.gemini.connector.ConnectionRule
import kotlinx.coroutines.launch

/**
 * @author HaronCode
 * @author kdk96
 */
object ResumePauseStrategy : LifecycleStrategy {

    override fun connect(
        lifecycleOwner: LifecycleOwner,
        rule: ConnectionRule
    ) {
        with(lifecycleOwner) {
            lifecycle.coroutineScope.launch {
                launch { whenResumed { rule.connect() } }
            }
        }
    }
}
