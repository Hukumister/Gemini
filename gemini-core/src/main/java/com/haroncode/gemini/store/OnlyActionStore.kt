package com.haroncode.gemini.store

import com.haroncode.gemini.core.elements.Bootstrapper
import com.haroncode.gemini.core.elements.EventProducer
import com.haroncode.gemini.core.elements.Middleware
import com.haroncode.gemini.core.elements.Reducer

open class OnlyActionStore<Action : Any, State : Any, Event : Any>(
    initialState: State,
    reducer: Reducer<State, Action>,
    middleware: Middleware<Action, State, Action>,
    bootstrapper: Bootstrapper<Action>? = null,
    eventProducer: EventProducer<State, Action, Event>? = null
) : BaseStore<Action, State, Event, Action>(
    initialState = initialState,
    reducer = reducer,
    middleware = middleware,
    bootstrapper = bootstrapper,
    eventProducer = eventProducer
)