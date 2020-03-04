package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.android.LifecycleStrategy
import com.haroncode.gemini.connector.StoreLifecycle
import com.haroncode.gemini.connector.StoreLifecycle.Event
import com.haroncode.gemini.coordinator.Coordinator
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.subscribers.DisposableSubscriber
import org.reactivestreams.Subscriber

class StoreLifecycleEventObserver(
    private val coordinator: Coordinator,
    private val lifecycleStrategy: LifecycleStrategy
) : LifecycleEventObserver {

    private val androidStoreLifecycle = AndroidStoreLifecycle()

    init {
        val coordinatorSubscriber = CoordinatorSubscriber(coordinator)
        androidStoreLifecycle.subscribe(coordinatorSubscriber)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        lifecycleStrategy.handle(source, event)
            ?.let(androidStoreLifecycle::postAction)
    }

    private class CoordinatorSubscriber(
        private val coordinator: Coordinator
    ) : DisposableSubscriber<Event>() {

        override fun onComplete() = onNext(Event.COMPLETE)

        override fun onNext(event: Event) = coordinator.invoke(event)

        override fun onError(throwable: Throwable) = onNext(Event.COMPLETE)
    }

    private class AndroidStoreLifecycle : StoreLifecycle {

        private val processor = BehaviorProcessor.create<Event>()

        override fun subscribe(subscriber: Subscriber<in Event>) = processor
            .startWith(Event.START)
            .distinctUntilChanged()
            .subscribe(subscriber)

        fun postAction(event: Event) = processor.onNext(event)
    }
}
