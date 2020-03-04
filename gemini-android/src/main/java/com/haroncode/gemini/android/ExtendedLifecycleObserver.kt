package com.haroncode.gemini.android

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * @author kdk96.
 */
interface ExtendedLifecycleObserver : DefaultLifecycleObserver {
    fun onFinish(owner: LifecycleOwner) = Unit
}
