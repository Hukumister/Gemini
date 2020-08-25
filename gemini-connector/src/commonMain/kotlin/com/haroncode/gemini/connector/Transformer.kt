package com.haroncode.gemini.connector

import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface Transformer<in In, out Out> {

    fun transform(input: Flow<In>): Flow<Out>
}

fun <T> identityTransformer() = IdentityTransformer as Transformer<T, T>

object IdentityTransformer : Transformer<Nothing, Nothing> {
    override fun transform(input: Flow<Nothing>): Flow<Nothing> = input
}
