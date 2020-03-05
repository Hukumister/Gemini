package com.haroncode.gemini.android.extended

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

/**
 * @author kdk96.
 */
open class ExtendedLifecycleFragment(
    @LayoutRes contentLayoutId: Int = 0
) : Fragment(contentLayoutId) {

    private var instanceStateSaved = false

    private val extendedLifecycleObservers = mutableSetOf<ExtendedLifecycleObserver>()

    private val extendedLifecycle = ExtendedLifecycle(
        lifecycle = super.getLifecycle(),
        extendedLifecycleObserversHolder = object :
            ExtendedLifecycleObserversHolder {
            override fun add(observer: ExtendedLifecycleObserver) {
                extendedLifecycleObservers.add(observer)
            }

            override fun remove(observer: ExtendedLifecycleObserver) {
                extendedLifecycleObservers.remove(observer)
            }
        }
    )

    override fun getLifecycle(): Lifecycle = extendedLifecycle

    override fun onStart() {
        super.onStart()
        instanceStateSaved = false
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        val isFinishing = activity?.isFinishing == true || isRealRemoving

        if (isFinishing) extendedLifecycleObservers.forEach { observer -> observer.onFinish(this) }
    }

    private val isRealRemoving: Boolean
        get() {
            // When we rotate device isRemoving() return true for fragment placed in backstack
            // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
            if (instanceStateSaved) {
                instanceStateSaved = false
                return false
            }
            return isRemoving || isAnyParentRemoving
        }

    private val isAnyParentRemoving: Boolean
        get() {
            var isAnyParentRemoving = false
            var parent = parentFragment
            while (!isAnyParentRemoving && parent != null) {
                isAnyParentRemoving = parent.isRemoving
                parent = parent.parentFragment
            }
            return isAnyParentRemoving
        }
}
