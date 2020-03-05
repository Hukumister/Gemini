package com.haroncode.gemini.store

import com.haroncode.gemini.core.elements.*

open class BaseStore<Action : Any, State : Any, Event : Any, Effect : Any>(
    initialState: State,
    reducer: Reducer<State, Effect>,
    middleware: Middleware<Action, State, Effect>,
    bootstrapper: Bootstrapper<Action>? = null,
    eventProducer: EventProducer<State, Effect, Event>? = null,
    errorHandler: ErrorHandler<State>? = null
) : AbstractStore<Action, State, Event, Effect>(
    initialState = initialState,
    reducer = reducer,
    middleware = middleware,
    bootstrapper = bootstrapper,
    eventProducer = eventProducer,
    errorHandler = errorHandler
)
