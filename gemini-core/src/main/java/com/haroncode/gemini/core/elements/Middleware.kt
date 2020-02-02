package com.haroncode.gemini.core.elements

import io.reactivex.Flowable

/**
 * @author HaronCode.
 */
typealias Middleware<Action, State, Effect> = (action: Action, state: State) -> Flowable<Effect>