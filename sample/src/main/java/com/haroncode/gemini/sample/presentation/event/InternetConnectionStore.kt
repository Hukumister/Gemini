package com.haroncode.gemini.sample.presentation.event

import com.haroncode.gemini.core.elements.Bootstrapper
import com.haroncode.gemini.core.elements.EventProducer
import com.haroncode.gemini.sample.domain.system.InternetStatusObserver
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Action
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Action.ChangeStatus
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Event
import com.haroncode.gemini.store.OnlyActionStore
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import javax.inject.Inject

class InternetConnectionStore @Inject constructor(
    internetStatusObserver: InternetStatusObserver
) : OnlyActionStore<Action, Unit, Event>(
    initialState = Unit,
    bootstrapper = BootstrapperImpl(internetStatusObserver),
    eventProducer = EventProducerImpl(),
    reducer = { _, _ -> Unit },
    middleware = { _, _ -> Flowable.empty() }
) {

    sealed class Action {

        data class ChangeStatus(val hasConnection: Boolean) : Action()
    }

    sealed class Event {
        data class Status(val hasConnection: Boolean) : Event()
    }

    class EventProducerImpl : EventProducer<Unit, Action, Event> {

        override fun invoke(state: Unit, action: Action): Event? {
            return if (action is ChangeStatus) {
                Event.Status(action.hasConnection)
            } else {
                null
            }
        }
    }

    class BootstrapperImpl(
        private val internetStatusObserver: InternetStatusObserver
    ) : Bootstrapper<Action> {

        override fun invoke(): Publisher<Action> = internetStatusObserver.observe()
            .map(::ChangeStatus)
    }
}