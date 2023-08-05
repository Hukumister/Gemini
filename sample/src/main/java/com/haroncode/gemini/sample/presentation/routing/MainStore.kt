package com.haroncode.gemini.sample.presentation.routing

import com.github.terrakok.cicerone.Router
import com.haroncode.gemini.sample.Screens
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.presentation.routing.MainStore.Action
import com.haroncode.gemini.store.OnlyActionStore
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@PerFragment
class MainStore @Inject constructor(
    private val router: Router
) : OnlyActionStore<Action, Unit, Action>(
    initialState = Unit,
    reducer = { _, _ -> Unit },
    middleware = { action, _ -> flowOf(action) },
    eventProducer = { _, action -> action }
) {

    enum class Action {
        COUNTER,
        AUTH,
    }

    init {
        eventFlow
            .onEach { event ->
                when (event) {
                    Action.COUNTER -> router.navigateTo(Screens.Counter())
                    Action.AUTH -> router.navigateTo(Screens.Auth())
                }
            }
            .launchIn(coroutineScope)
    }
}
