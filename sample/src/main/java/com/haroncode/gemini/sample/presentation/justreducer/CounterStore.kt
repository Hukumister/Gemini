package com.haroncode.gemini.sample.presentation.justreducer

import com.haroncode.gemini.element.Reducer
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore.Action
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore.State
import com.haroncode.gemini.store.ReducerStore
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

// it's very important to save instance of store between rotation of screen
@PerFragment
class CounterStore @Inject constructor() : ReducerStore<Action, State, Nothing>(
    initialState = State(),
    reducer = ReducerImpl(),
    coroutineDispatcher = Dispatchers.Main
) {

    sealed class Action {
        object Increment : Action()
        object Decrement : Action()
    }

    data class State(
        val count: Int = 0
    )

    class ReducerImpl : Reducer<State, Action> {

        override fun reduce(state: State, effect: Action) = when (effect) {
            is Action.Increment -> state.copy(count = state.count + 1)
            is Action.Decrement -> state.copy(count = state.count - 1)
        }
    }
}
