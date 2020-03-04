@file:Suppress("NOTHING_TO_INLINE")

package com.haroncode.gemini.dsl

import com.haroncode.gemini.connection.BaseConnectionRule
import com.haroncode.gemini.connection.EventListenerConnection
import com.haroncode.gemini.core.EventListener
import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.StoreView
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

typealias Transformer<In, Out> = (Flowable<In>) -> Flowable<Out>

typealias Mapper<In, Out> = (In) -> Out

private val IDENTITY_TRANSFORMER: Transformer<Any, Any> = { stream -> stream }

private val IDENTITY_FLOWABLE_TRANSFORMER = FlowableTransformer<Any, Any> { stream -> stream }

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityFlowableTransformer() = IDENTITY_FLOWABLE_TRANSFORMER as FlowableTransformer<T, T>

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityTransformer() = IDENTITY_TRANSFORMER as Transformer<T, T>

inline infix fun <T : Any> Publisher<T>.to(consumer: Consumer<T>) = BaseConnectionRule(
    consumer = consumer,
    publisher = this,
    transformer = identityFlowableTransformer()
)

inline infix fun <Out : Any, In : Any> Pair<Publisher<Out>, Consumer<In>>.with(
    crossinline transformer: Transformer<Out, In>
) = BaseConnectionRule(
    consumer = this.second,
    publisher = this.first,
    transformer = FlowableTransformer { stream -> transformer.invoke(stream) }
)

inline infix fun <Out : Any, In : Any> Pair<Publisher<Out>, Consumer<In>>.mapper(
    crossinline mapper: Mapper<Out, In>
) = BaseConnectionRule(
    consumer = this.second,
    publisher = this.first,
    transformer = FlowableTransformer { stream -> stream.map { input -> mapper.invoke(input) } }
)

inline infix fun <T : Any, R : Any> BaseConnectionRule<T, R>.observeOn(
    scheduler: Scheduler
): BaseConnectionRule<T, R> = BaseConnectionRule(
    consumer = this.consumer,
    publisher = this.publisher,
    transformer = FlowableTransformer { stream ->
        Flowable.fromPublisher(this.transformer.apply(stream))
            .observeOn(scheduler)
    }
)

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

inline infix fun <Event : Any> Store<*, *, Event>.eventTo(
    eventListener: EventListener<Event>
) = EventListenerConnection(
    eventPublisher = this.eventSource,
    eventListener = eventListener
)