package com.haroncode.gemini.core.elements

/**
 * @author HaronCode.
 */
typealias EventProducer<State, Effect, Event> = (state: State, effect: Effect) -> Event?