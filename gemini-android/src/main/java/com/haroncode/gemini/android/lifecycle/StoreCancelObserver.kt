package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.AutoCancelStoreRule

/**
 * @author HaronCode
 * @author kdk96
 */
internal class StoreCancelObserver(
    private val autoCancelStoreRuleCollection: Collection<AutoCancelStoreRule>
) : ExtendedLifecycleObserver() {

    override fun onFinish(owner: LifecycleOwner) {
        autoCancelStoreRuleCollection.forEach(AutoCancelStoreRule::cancel)
    }
}
