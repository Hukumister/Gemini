package com.haroncode.gemini.reactive

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface Consumer<T> {

    fun accept(input: T)
}
