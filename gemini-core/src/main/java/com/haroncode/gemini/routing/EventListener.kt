package com.haroncode.gemini.routing

/**
 * @author HaronCode.
 */
interface EventListener<Event : Any> {

    fun onEvent(event: Event)
}