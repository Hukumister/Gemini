package com.haroncode.gemini.core.elements

import org.reactivestreams.Publisher

/**
 * @author HaronCode.
 */
typealias Middleware<Action, State, Effect> = (action: Action, state: State) -> Publisher<Effect>
