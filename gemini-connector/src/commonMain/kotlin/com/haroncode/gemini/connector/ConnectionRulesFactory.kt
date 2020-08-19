package com.haroncode.gemini.connector

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface ConnectionRulesFactory<P : Any> {

    fun create(param: P): Collection<ConnectionRule>
}
