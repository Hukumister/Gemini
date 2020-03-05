package com.haroncode.gemini.android.extended

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.android.extended.AndroidLifecycleEvent.*

abstract class EventExtendedLifecycleObserver : ExtendedLifecycleObserver {

    abstract fun onStateChanged(source: LifecycleOwner, event: AndroidLifecycleEvent)

    final override fun onFinish(owner: LifecycleOwner) = onStateChanged(owner, ON_REAL_REMOVING)

    final override fun onCreate(owner: LifecycleOwner) = onStateChanged(owner, ON_CREATE)

    final override fun onResume(owner: LifecycleOwner) = onStateChanged(owner, ON_RESUME)

    final override fun onPause(owner: LifecycleOwner) = onStateChanged(owner, ON_PAUSE)

    final override fun onStart(owner: LifecycleOwner) = onStateChanged(owner, ON_START)

    final override fun onStop(owner: LifecycleOwner) = onStateChanged(owner, ON_STOP)

    final override fun onDestroy(owner: LifecycleOwner) = onStateChanged(owner, ON_DESTROY)
}
