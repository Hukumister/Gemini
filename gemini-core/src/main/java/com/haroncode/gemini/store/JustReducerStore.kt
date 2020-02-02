package com.haroncode.gemini.store

import io.reactivex.Flowable
import com.haroncode.gemini.core.elements.Bootstrapper
import com.haroncode.gemini.core.elements.EventProducer
import com.haroncode.gemini.core.elements.Middleware
import com.haroncode.gemini.core.elements.Reducer

open class JustReducerStore<Action : Any, State : Any, Event : Any>(
        initialState: State,
        reducer: Reducer<State, Action>,
        bootstrapper: Bootstrapper<Action>? = null,
        eventProducer: EventProducer<State, Action, Event>? = null
) : OnlyActionStore<Action, State, Event>(
    initialState = initialState,
    reducer = reducer,
    middleware = BypassMiddleware<State, Action>(),
    bootstrapper = bootstrapper,
    eventProducer = eventProducer
) {

    class BypassMiddleware<State : Any, Action : Any> : Middleware<Action, State, Action> {

        override fun invoke(action: Action, state: State): Flowable<Action> = Flowable.just(action)
    }

}