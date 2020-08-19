package com.haroncode.gemini.store

import com.haroncode.gemini.element.Bootstrapper
import com.haroncode.gemini.element.ErrorHandler
import com.haroncode.gemini.element.EventProducer
import com.haroncode.gemini.element.Middleware
import com.haroncode.gemini.element.Reducer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * @author HaronCode
 * @author kdk96
 */
open class ReducerStore<Action : Any, State : Any, Event : Any>(
    initialState: State,
    reducer: Reducer<State, Action>,
    bootstrapper: Bootstrapper<Action>? = null,
    eventProducer: EventProducer<State, Action, Event>? = null,
    errorHandler: ErrorHandler<State>? = null,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : OnlyActionStore<Action, State, Event>(
    initialState = initialState,
    reducer = reducer,
    middleware = BypassMiddleware(),
    bootstrapper = bootstrapper,
    eventProducer = eventProducer,
    errorHandler = errorHandler,
    coroutineDispatcher = coroutineDispatcher
) {

    private class BypassMiddleware<Action : Any, State : Any> : Middleware<Action, State, Action> {

        override fun invoke(action: Action, state: State): Flow<Action> = flowOf(action)
    }
}
