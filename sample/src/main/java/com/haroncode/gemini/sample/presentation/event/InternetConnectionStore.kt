package com.haroncode.gemini.sample.presentation.event

import com.haroncode.gemini.element.Bootstrapper
import com.haroncode.gemini.element.EventProducer
import com.haroncode.gemini.sample.domain.repository.ConnectivityRepository
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Action
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Action.ChangeStatus
import com.haroncode.gemini.sample.presentation.event.InternetConnectionStore.Event
import com.haroncode.gemini.store.OnlyActionStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InternetConnectionStore @Inject constructor(
    connectivityRepository: ConnectivityRepository
) : OnlyActionStore<Action, Unit, Event>(
    initialState = Unit,
    bootstrapper = BootstrapperImpl(connectivityRepository),
    eventProducer = EventProducerImpl(),
    reducer = { _, _ -> Unit },
    middleware = { action, _ -> flowOf(action) }
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

        override fun invoke(): Flow<Action> = connectivityRepository.observeConnectionState()
            .map { ChangeStatus(it) }
    }
}
