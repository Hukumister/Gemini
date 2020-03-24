package com.haroncode.gemini.store

import com.haroncode.gemini.core.elements.*
import io.reactivex.Flowable

open class JustReducerStore<Action : Any, State : Any, Event : Any>(
    initialState: State,
    reducer: Reducer<State, Action>,
    bootstrapper: Bootstrapper<Action>? = null,
    eventProducer: EventProducer<State, Action, Event>? = null,
    errorHandler: ErrorHandler<State>? = null
) : OnlyActionStore<Action, State, Event>(
    initialState = initialState,
    reducer = reducer,
    middleware = BypassMiddleware<State, Action>(),
    bootstrapper = bootstrapper,
    eventProducer = eventProducer,
    errorHandler = errorHandler
) {

    class BypassMiddleware<State : Any, Action : Any> : Middleware<Action, State, Action> {

        override fun invoke(action: Action, state: State): Flowable<Action> = Flowable.just(action)
    }
}
