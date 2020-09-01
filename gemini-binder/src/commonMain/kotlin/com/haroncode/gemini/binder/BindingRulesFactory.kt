package com.haroncode.gemini.binder

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface BindingRulesFactory<P : Any> {

    fun create(param: P): Collection<BindingRule>
}
