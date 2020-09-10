package com.haroncode.gemini.lifecycle

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.BaseBindingRule

/**
 * @author HaronCode
 * @author kdk96
 */
interface LifecycleStrategy {

    fun bind(
        lifecycleOwner: LifecycleOwner,
        rules: Collection<BaseBindingRule<*, *>>
    )
}
