package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.whenResumed
import com.haroncode.gemini.binder.BaseBindingRule
import kotlinx.coroutines.launch

/**
 * @author HaronCode
 * @author kdk96
 */
object ResumePauseStrategy : LifecycleStrategy {

    override fun connect(
        lifecycleOwner: LifecycleOwner,
        rules: Collection<BaseBindingRule<*, *>>
    ) {
        with(lifecycleOwner) {
            lifecycle.coroutineScope.launch {
                rules.forEach { rule ->
                    launch { whenResumed { rule.bind() } }
                }
            }
        }
    }
}
