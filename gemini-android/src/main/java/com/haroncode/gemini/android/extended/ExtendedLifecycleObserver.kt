package com.haroncode.gemini.android.extended

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner

/**
 * @author kdk96.
 */
abstract class ExtendedLifecycleObserver(
    private val savedStateRegistryOwner: SavedStateRegistryOwner
) : DefaultLifecycleObserver, SavedStateRegistry.SavedStateProvider {

    private var instanceStateSaved = false

    abstract fun onFinish(owner: LifecycleOwner)

    @CallSuper
    override fun onCreate(owner: LifecycleOwner) {
        savedStateRegistryOwner.savedStateRegistry.registerSavedStateProvider("${KEY}_${hashCode()}", this)
    }

    @CallSuper
    override fun onStart(owner: LifecycleOwner) {
        instanceStateSaved = false
    }

    @CallSuper
    override fun onResume(owner: LifecycleOwner) {
        instanceStateSaved = false
    }

    final override fun saveState(): Bundle {
        instanceStateSaved = true
        return Bundle()
    }

    @CallSuper
    override fun onDestroy(owner: LifecycleOwner) {
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
        private const val KEY = "ExtendedLifecycleObserver"
    }
}
