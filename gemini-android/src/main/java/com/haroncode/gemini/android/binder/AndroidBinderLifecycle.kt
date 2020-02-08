package com.haroncode.gemini.android.binder

import androidx.lifecycle.Lifecycle
import com.haroncode.gemini.binder.BinderLifecycle
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Subscriber

class AndroidBinderLifecycle(
    lifecycle: Lifecycle,
    bindingStrategy: BindingStrategy
) : BinderLifecycle {

    private val processor = BehaviorProcessor.create<BinderLifecycle.State>()

    init {
        val lifecycleObserver = bindingStrategy.handle(processor::onNext)
        lifecycle.addObserver(lifecycleObserver)
    }

    override fun subscribe(subscriber: Subscriber<in BinderLifecycle.State>) = processor.subscribe(subscriber)
}