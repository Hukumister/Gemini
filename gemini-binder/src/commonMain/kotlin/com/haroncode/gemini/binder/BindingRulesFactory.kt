package com.haroncode.gemini.binder

import com.haroncode.gemini.binder.rule.BindingRule

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface BindingRulesFactory<P : Any> {

    fun create(param: P): Collection<BindingRule>
}
