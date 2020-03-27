package com.haroncode.gemini.core

/**
 * @author HaronCode.
 */
interface StoreEventListener<Event : Any> {

    /**
     * The method will be invoked when the store will emit a new event.
     */
    fun onEvent(event: Event)
}
