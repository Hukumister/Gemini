package com.haroncode.gemini.element

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface EventProducer<State, Effect, Event> {

    fun produce(state: State, effect: Effect): Event?
}
