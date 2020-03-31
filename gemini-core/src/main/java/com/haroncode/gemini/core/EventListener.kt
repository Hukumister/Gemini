package com.haroncode.gemini.core

/**
 * @author HaronCode.
 */
@Deprecated(
    message = "Use StoreEventListener instead",
    replaceWith = ReplaceWith(expression = "StoreEventListener<>")
)
typealias EventListener<Event> = StoreEventListener<Event>
