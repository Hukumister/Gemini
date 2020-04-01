package com.haroncode.gemini.sample.presentation.event

import com.haroncode.gemini.core.elements.Bootstrapper
import com.haroncode.gemini.core.elements.EventProducer
import com.haroncode.gemini.sample.domain.repository.ConnectivityRepository
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Action
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Action.ChangeStatus
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Event
import com.haroncode.gemini.store.OnlyActionStore
import io.reactivex.Flowable
import javax.inject.Inject
import org.reactivestreams.Publisher

class InternetConnectionStore @Inject constructor(
    connectivityRepository: ConnectivityRepository
) : OnlyActionStore<Action, Unit, Event>(
    initialState = Unit,
    bootstrapper = BootstrapperImpl(connectivityRepository),
    eventProducer = EventProducerImpl(),
    reducer = { _, _ -> Unit },
    middleware = { action, _ -> Flowable.just(action) }
) {

    sealed class Action {
        data class ChangeStatus(val hasConnection: Boolean) : Action()
    }

    sealed class Event {
        data class Status(val hasConnection: Boolean) : Event()
    }

    class EventProducerImpl : EventProducer<Unit, Action, Event> {

        override fun invoke(state: Unit, action: Action): Event? = when (action) {
            is ChangeStatus -> Event.Status(action.hasConnection)
        }
    }

    class BootstrapperImpl(
        private val connectivityRepository: ConnectivityRepository
    ) : Bootstrapper<Action> {

        override fun invoke(): Publisher<Action> = connectivityRepository.observeConnectionState()
            .map(::ChangeStatus)
    }
}
