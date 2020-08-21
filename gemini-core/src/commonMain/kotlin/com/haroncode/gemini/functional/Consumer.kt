package com.haroncode.gemini.functional

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface Consumer<T> {

    fun accept(value: T)
}
