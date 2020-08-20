package com.haroncode.gemini.element

/**
 * @author HaronCode
 * @author kdk96
 */
typealias EventProducer<State, Effect, Event> = (state: State, effect: Effect) -> Event?
