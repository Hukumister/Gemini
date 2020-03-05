package com.haroncode.gemini.android.extended

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * @author kdk96.
 */
interface ExtendedLifecycleObserver : DefaultLifecycleObserver {

    fun onFinish(owner: LifecycleOwner)
}
