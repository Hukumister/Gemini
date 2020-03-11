package com.haroncode.gemini.android.extended

import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.extended.AndroidLifecycleEvent.*

abstract class EventExtendedLifecycleObserver(
    savedStateRegistryOwner: SavedStateRegistryOwner
) : ExtendedLifecycleObserver(savedStateRegistryOwner) {

    abstract fun onStateChanged(source: LifecycleOwner, event: AndroidLifecycleEvent)

    final override fun onFinish(owner: LifecycleOwner) = onStateChanged(owner, ON_FINISH)

    final override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        onStateChanged(owner, ON_CREATE)
    }

    final override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        onStateChanged(owner, ON_RESUME)
    }

    final override fun onPause(owner: LifecycleOwner) = onStateChanged(owner, ON_PAUSE)

    final override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        onStateChanged(owner, ON_START)
    }

    final override fun onStop(owner: LifecycleOwner) = onStateChanged(owner, ON_STOP)

    final override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        onStateChanged(owner, ON_DESTROY)
    }
}
