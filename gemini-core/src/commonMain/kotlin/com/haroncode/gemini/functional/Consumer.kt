package com.haroncode.gemini.functional

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface Consumer<in T> {

    fun accept(value: T)
}
