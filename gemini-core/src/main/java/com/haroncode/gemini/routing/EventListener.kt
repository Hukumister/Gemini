package com.haroncode.gemini.routing

interface EventListener<Event : Any> {

    fun onEvent(event: Event)

}