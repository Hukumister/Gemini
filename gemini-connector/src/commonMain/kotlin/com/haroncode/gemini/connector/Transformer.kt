package com.haroncode.gemini.connector

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface Transformer<in In, out Out> {

    fun transform(input: Flow<In>): Flow<Out>
}

internal object IdentityTransformer : Transformer<Any, Nothing> {
    override fun transform(input: Flow<Any>): Flow<Nothing> = emptyFlow()
}
