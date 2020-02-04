package com.haroncode.gemini.core.elements

/**
 * @author HaronCode.
 */
typealias StoreNavigator<State, Event> = (state: State, event: Event) -> Unit