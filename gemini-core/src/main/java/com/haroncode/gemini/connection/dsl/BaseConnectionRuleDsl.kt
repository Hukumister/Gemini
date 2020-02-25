package com.haroncode.gemini.connection.dsl

import com.haroncode.gemini.connection.BaseConnectionRule
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

typealias Transformer<In, Out> = (Flowable<In>) -> Flowable<Out>

private val IDENTITY_TRANSFORMER: Transformer<Any, Any> = { stream -> stream }

private val IDENTITY_FLOWABLE_TRANSFORMER = FlowableTransformer<Any, Any> { stream -> stream }

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityFlowableTransformer() = IDENTITY_FLOWABLE_TRANSFORMER as FlowableTransformer<T, T>

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityTransformer() = IDENTITY_TRANSFORMER as Transformer<T, T>

inline infix fun <Out : Any, In : Any> Pair<Publisher<Out>, Consumer<In>>.with(
    crossinline transformer: Transformer<Out, In>
) = BaseConnectionRule(
    consumer = this.second,
    publisher = this.first,
    transformer = FlowableTransformer { stream -> transformer.invoke(stream) }
)

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Any> schedulerTransformer(
    scheduler: Scheduler
): Transformer<T, T> = { input -> input.observeOn(scheduler) }

@Suppress("NOTHING_TO_INLINE")
inline infix fun <T : Any> BaseConnectionRule<T, T>.decorate(
    noinline transformer: Transformer<T, T>
): BaseConnectionRule<T, T> = BaseConnectionRule(
    consumer = this.consumer,
    publisher = this.publisher,
    transformer = FlowableTransformer { stream ->
        Flowable.fromPublisher(this.transformer.apply(stream))
            .compose(transformer)
    }
)
