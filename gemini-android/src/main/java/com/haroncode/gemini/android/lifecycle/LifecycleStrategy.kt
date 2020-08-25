package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.connector.BaseConnectionRule

/**
 * @author HaronCode
 * @author kdk96
 */
interface LifecycleStrategy {

    fun connect(
        lifecycleOwner: LifecycleOwner,
        rules: Collection<BaseConnectionRule<*, *>>
    )
}
