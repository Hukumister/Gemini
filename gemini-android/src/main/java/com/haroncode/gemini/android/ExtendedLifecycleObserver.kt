package com.haroncode.gemini.android

import androidx.lifecycle.DefaultLifecycleObserver

interface ExtendedLifecycleObserver : DefaultLifecycleObserver {
    fun onFinish()
}
