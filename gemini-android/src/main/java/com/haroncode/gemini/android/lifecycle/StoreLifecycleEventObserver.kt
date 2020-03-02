package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.android.LifecycleStrategy
import com.haroncode.gemini.connector.Coordinator
import com.haroncode.gemini.connector.LifecycleEventSource
import com.haroncode.gemini.connector.StoreLifecycle
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Subscriber

class StoreLifecycleEventObserver(
    coordinator: Coordinator,
    private val lifecycleStrategy: LifecycleStrategy
) : LifecycleEventObserver {

    private val androidStoreLifecycle = AndroidStoreLifecycle()

    init {
        val lifecycleEventSource = LifecycleEventSource(androidStoreLifecycle)
        lifecycleEventSource.start(coordinator)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) = lifecycleStrategy
        .handle(source, event)
        .subscribe(androidStoreLifecycle)

    private class AndroidStoreLifecycle : StoreLifecycle, DisposableMaybeObserver<StoreLifecycle.Event>() {

        private val processor = BehaviorProcessor.create<StoreLifecycle.Event>()

        override fun subscribe(subscriber: Subscriber<in StoreLifecycle.Event>) = processor
            .startWith(StoreLifecycle.Event.START)
            .distinctUntilChanged()
            .subscribe(subscriber)

        override fun onSuccess(event: StoreLifecycle.Event) = processor.onNext(event)

        override fun onComplete() = Unit

        override fun onError(throwable: Throwable) = processor.onNext(StoreLifecycle.Event.COMPLETE)
    }
}
