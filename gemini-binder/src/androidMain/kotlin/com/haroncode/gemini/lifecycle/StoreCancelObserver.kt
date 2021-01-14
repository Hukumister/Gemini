package com.haroncode.gemini.lifecycle

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.rule.AutoCancelStoreRule

/**
 * @author HaronCode
 * @author kdk96
 */
@Deprecated(
    message = "Will be removed in upcoming release, have to use getStore extension to retain and cancel store instance in ViewModel",
    replaceWith = ReplaceWith("getStore(provider)", imports = ["com.haroncode.gemini.keeper.getStore"])
)
internal class StoreCancelObserver(
    private val autoCancelStoreRuleCollection: Collection<AutoCancelStoreRule>
) : ExtendedLifecycleObserver() {

    override fun onFinish(owner: LifecycleOwner) {
        autoCancelStoreRuleCollection.forEach(AutoCancelStoreRule::cancel)
    }
}
