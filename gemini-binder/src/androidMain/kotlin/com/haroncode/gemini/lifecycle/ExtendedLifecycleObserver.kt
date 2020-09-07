package com.haroncode.gemini.lifecycle
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner

/**
 * @author kdk96
 */
internal abstract class ExtendedLifecycleObserver : LifecycleObserver, SavedStateRegistry.SavedStateProvider {

    private var instanceStateSaved = false

    open fun onFinish(owner: LifecycleOwner) = Unit

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(owner: LifecycleOwner) {
        if (owner is SavedStateRegistryOwner) {
            owner.savedStateRegistry.registerSavedStateProvider("${KEY}_${hashCode()}", this)
        }
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart(owner: LifecycleOwner) {
        instanceStateSaved = false
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume(owner: LifecycleOwner) {
        instanceStateSaved = false
    }

    final override fun saveState(): Bundle {
        instanceStateSaved = true
        return Bundle()
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(owner: LifecycleOwner) {
        val isFinishing = when (owner) {
            is AppCompatActivity -> owner.isFinishing
            is Fragment -> owner.activity?.isFinishing == true || owner.isRealRemoving
            else -> false
        }
        if (isFinishing) onFinish(owner)
    }

    private val Fragment.isRealRemoving: Boolean
        get() {
            // When we rotate device isRemoving() return true for fragment placed in backstack
            // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
            if (instanceStateSaved) {
                instanceStateSaved = false
                return false
            }
            return isRemoving || isAnyParentRemoving
        }

    private val Fragment.isAnyParentRemoving: Boolean
        get() {
            var isAnyParentRemoving = false
            var parent = parentFragment
            while (!isAnyParentRemoving && parent != null) {
                isAnyParentRemoving = parent.isRemoving
                parent = parent.parentFragment
            }
            return isAnyParentRemoving
        }

    companion object {
        private const val KEY = "com.haroncode.gemini.lifecycle.ExtendedLifecycleObserver"
    }
}
