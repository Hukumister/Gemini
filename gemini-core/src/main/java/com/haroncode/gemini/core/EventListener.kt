package com.haroncode.gemini.core

/**
 * @author HaronCode.
 */
interface EventListener<Event : Any> {

    fun onEvent(event: Event)
}
