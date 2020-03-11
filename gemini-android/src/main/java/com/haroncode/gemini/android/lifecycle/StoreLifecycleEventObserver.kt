package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.LifecycleStrategy
import com.haroncode.gemini.android.extended.AndroidLifecycleEvent
import com.haroncode.gemini.android.extended.EventExtendedLifecycleObserver
import com.haroncode.gemini.connector.StoreConnector
import com.haroncode.gemini.connector.StoreLifecycle
import com.haroncode.gemini.connector.StoreLifecycle.Event
import com.haroncode.gemini.coordinator.Coordinator
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Subscriber

class StoreLifecycleEventObserver(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    storeConnector: StoreConnector,
    private val lifecycleStrategy: LifecycleStrategy
) : EventExtendedLifecycleObserver(savedStateRegistryOwner) {

    private val androidStoreLifecycle = AndroidStoreLifecycle()

    init {
        val coordinator = Coordinator(androidStoreLifecycle, storeConnector)
        coordinator.start()
    }

    override fun onStateChanged(source: LifecycleOwner, event: AndroidLifecycleEvent) {
        lifecycleStrategy.handle(source, event)?.let(androidStoreLifecycle::postAction)
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
