package com.haroncode.gemini.event

/**
 * @author HaronCode.
 */
interface EventListener<Event : Any> {

    fun onEvent(event: Event)
}
