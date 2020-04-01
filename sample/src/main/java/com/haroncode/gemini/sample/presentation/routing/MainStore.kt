package com.haroncode.gemini.sample.presentation.routing

import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.presentation.routing.MainStore.Action
import com.haroncode.gemini.store.OnlyActionStore
import io.reactivex.Flowable
import javax.inject.Inject

@PerFragment
class MainStore @Inject constructor() : OnlyActionStore<Action, Unit, Action>(
    initialState = Unit,
    reducer = { _, _ -> Unit },
    middleware = { action, _ -> Flowable.just(action) },
    eventProducer = { _, action -> action }
) {

    enum class Action {
        COUNTER,
        AUTH,
    }
}
