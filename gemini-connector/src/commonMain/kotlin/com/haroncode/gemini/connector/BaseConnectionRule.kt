package com.haroncode.gemini.connector

import com.haroncode.gemini.util.Consumer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

open class BaseConnectionRule<Out : Any, In : Any>(
    val consumer: Consumer<In>,
    val flow: Flow<Out>,
    val transformer: Transformer<Out, In>
) : AbstractConnectionRule() {

    override suspend fun connect() = flow
        .let(transformer)
        .collect { consumer.accept(it) }
}
