package com.haroncode.gemini

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface StoreEventListener<Event : Any> {
    fun onEvent(event: Event)
}
