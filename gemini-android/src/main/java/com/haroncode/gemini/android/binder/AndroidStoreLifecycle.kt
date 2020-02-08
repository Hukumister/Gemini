package com.haroncode.gemini.android.binder

import androidx.lifecycle.Lifecycle
import com.haroncode.gemini.core.StoreLifecycle
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Subscriber

class AndroidStoreLifecycle(
    lifecycle: Lifecycle,
    bindingStrategy: BindingStrategy
) : StoreLifecycle {

    private val processor = BehaviorProcessor.create<StoreLifecycle.State>()

    init {
        val lifecycleObserver = bindingStrategy.handle(processor::onNext)
        lifecycle.addObserver(lifecycleObserver)
    }

    override fun subscribe(subscriber: Subscriber<in StoreLifecycle.State>) = processor.subscribe(subscriber)
}