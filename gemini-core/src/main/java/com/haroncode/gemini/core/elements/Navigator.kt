package com.haroncode.gemini.core.elements

/**
 * @author HaronCode.
 */
typealias Navigator<State, Event> = (state: State, event: Event) -> Unit