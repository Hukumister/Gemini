package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.Lifecycle
import com.haroncode.gemini.android.BindingStrategy
import com.haroncode.gemini.core.StoreLifecycle
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Subscriber

class AndroidStoreLifecycle(
    lifecycle: Lifecycle,
    bindingStrategy: BindingStrategy
) : StoreLifecycle {

    private val processor = BehaviorProcessor.create<StoreLifecycle.Event>()

    init {
        val lifecycleEventObserver =
            RxLifecycleEventObserver()
        lifecycle.addObserver(lifecycleEventObserver)

        Flowable.fromPublisher(lifecycleEventObserver)
            .concatMap { event ->
                val storeLifecycleEvent = bindingStrategy.handle(event)
                storeEventOrEmpty(storeLifecycleEvent)
            }
            .subscribe(processor)
    }

    override fun subscribe(subscriber: Subscriber<in StoreLifecycle.Event>) = processor.subscribe(subscriber)

    private fun storeEventOrEmpty(
        storeLifecycleEvent: StoreLifecycle.Event?
    ) = if (storeLifecycleEvent != null) {
        Flowable.just(storeLifecycleEvent)
    } else {
        Flowable.empty<StoreLifecycle.Event>()
    }
}