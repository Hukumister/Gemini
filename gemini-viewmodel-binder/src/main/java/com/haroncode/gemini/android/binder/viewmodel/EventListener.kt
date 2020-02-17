package com.haroncode.gemini.android.binder.viewmodel

/**
 * @author HaronCode.
 */
interface EventListener<Event : Any> {

    fun onEvent(event: Event)
}