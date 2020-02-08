package com.haroncode.gemini.android.binder.strategies

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.BinderLifecycle.State
import com.haroncode.gemini.android.binder.BindingStrategy
import com.haroncode.gemini.android.binder.StateSender

object ResumePauseStrategy : BindingStrategy {

    override fun handle(sender: StateSender) = object : DefaultLifecycleObserver {

        override fun onResume(owner: LifecycleOwner) = sender.invoke(State.START)

        override fun onPause(owner: LifecycleOwner) = sender.invoke(State.STOP)
    }
}