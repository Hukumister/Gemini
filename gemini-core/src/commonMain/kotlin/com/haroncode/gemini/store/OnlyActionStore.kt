package com.haroncode.gemini.store

import com.haroncode.gemini.element.Bootstrapper
import com.haroncode.gemini.element.ErrorHandler
import com.haroncode.gemini.element.EventProducer
import com.haroncode.gemini.element.Middleware
import com.haroncode.gemini.element.Reducer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author HaronCode
 * @author kdk96
 */
open class OnlyActionStore<Action : Any, State : Any, Event : Any>(
    initialState: State,
    reducer: Reducer<State, Action>,
    middleware: Middleware<Action, State, Action>,
    bootstrapper: Bootstrapper<Action>? = null,
    eventProducer: EventProducer<State, Action, Event>? = null,
    errorHandler: ErrorHandler<State>? = null,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseStore<Action, State, Event, Action>(
    initialState = initialState,
    reducer = reducer,
    middleware = middleware,
    bootstrapper = bootstrapper,
    eventProducer = eventProducer,
    errorHandler = errorHandler,
    coroutineDispatcher = coroutineDispatcher
)
