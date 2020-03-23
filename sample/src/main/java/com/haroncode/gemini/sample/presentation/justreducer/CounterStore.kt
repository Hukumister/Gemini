package com.haroncode.gemini.sample.presentation.justreducer

import com.haroncode.gemini.core.elements.Reducer
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore.Action
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore.State
import com.haroncode.gemini.store.JustReducerStore
import javax.inject.Inject

// it's very important to save instance of store between rotation of screen
@PerFragment
class CounterStore @Inject constructor() : JustReducerStore<Action, State, Nothing>(
    initialState = State(),
    reducer = ReducerImpl()
) {

    sealed class Action {
        object Increment : Action()
    }

    data class State(
        val count: Int = 0
    )

    class ReducerImpl : Reducer<State, Action> {

        override fun invoke(state: State, action: Action) = when (action) {
            is Action.Increment -> state.copy(count = state.count + 1)
        }
    }
}
