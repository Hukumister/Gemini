package com.haroncode.gemini.connector

import com.haroncode.gemini.StoreEventListener
import com.haroncode.gemini.element.Store
import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

sealed class ConnectionRule

/**
 *
 */
class AutoCancelStoreRule(
    private val store: Store<*, *, *>
) : ConnectionRule() {

    fun cancel() = store.coroutineScope.cancel()
}

/**
 *
 */
open class BaseConnectionRule<Out : Any, In : Any>(
    val consumer: Consumer<In>,
    val flow: Flow<Out>,
    val transformer: Transformer<Out, In>
) : ConnectionRule() {

    suspend fun connect() = flow
        .let(transformer::transform)
        .collect { consumer.accept(it) }
}

/**
 *
 */
infix fun <T : Any> Flow<T>.connectWith(
    consumer: Consumer<T>
): BaseConnectionRule<T, T> = BaseConnectionRule(
    consumer = consumer,
    flow = this,
    transformer = identityTransformer()
)

/**
 *
 */
infix fun <Out : Any, In : Any> Flow<Out>.connectWith(
    consumer: Consumer<In>
): Pair<Flow<Out>, Consumer<In>> = this to consumer

/**
 *
 */
infix fun <Out : Any, In : Any> Flow<Out>.connectEventWith(
    eventListener: StoreEventListener<In>
): Pair<Flow<Out>, Consumer<In>> = this to Consumer { event -> eventListener.onEvent(event) }

/**
 *
 */
infix fun <T : Any> Flow<T>.connectEventWith(
    eventListener: StoreEventListener<T>
): BaseConnectionRule<T, T> = BaseConnectionRule(
    consumer = { event -> eventListener.onEvent(event) },
    flow = this,
    transformer = identityTransformer()
)

/**
 *
 */
infix fun <Out : Any, In : Any> Pair<Flow<Out>, Consumer<In>>.transform(
    transformer: Transformer<Out, In>
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = second,
    flow = first,
    transformer = { flow -> flow.let(transformer::transform) }
)

/**
 *
 */
infix fun <Out : Any, In : Any> BaseConnectionRule<Out, In>.transform(
    outputTransformer: Transformer<In, In>
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = consumer,
    flow = flow,
    transformer = { flow ->
        flow
            .let(transformer::transform)
            .let(outputTransformer::transform)
    }
)

/**
 *
 */
infix fun <Out : Any, In : Any> Pair<Flow<Out>, Consumer<In>>.map(
    mapper: suspend (Out) -> In
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = second,
    flow = first,
    transformer = { flow -> flow.map(mapper::invoke) }
)

/**
 *
 */
infix fun <Out : Any, In : Any> BaseConnectionRule<Out, In>.map(
    mapper: suspend (Out) -> In
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = consumer,
    flow = flow,
    transformer = { flow -> flow.map(mapper::invoke) }
)
