@file:Suppress("NOTHING_TO_INLINE")

package com.haroncode.gemini.dsl

import com.haroncode.gemini.connection.BaseConnectionRule
import com.haroncode.gemini.connection.EventListenerConnection
import com.haroncode.gemini.connection.util.Mapper
import com.haroncode.gemini.connection.util.Transformer
import com.haroncode.gemini.connection.util.identityFlowableTransformer
import com.haroncode.gemini.core.EventListener
import com.haroncode.gemini.core.Store
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

inline infix fun <T : Any> Publisher<T>.connectTo(consumer: Consumer<T>) = BaseConnectionRule(
    consumer = consumer,
    publisher = this,
    transformer = identityFlowableTransformer()
)

inline infix fun <T : Any, R : Any> Publisher<T>.connectTo(consumer: Consumer<R>) = this to consumer

inline infix fun <Out : Any, In : Any> BaseConnectionRule<Out, In>.with(
    noinline transformer: Transformer<In, In>
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = this.consumer,
    publisher = this.publisher,
    transformer = FlowableTransformer { stream ->
        Flowable.fromPublisher(this.transformer.apply(stream))
            .compose(transformer)
    }
)

inline infix fun <Out : Any, In : Any> Pair<Publisher<Out>, Consumer<In>>.with(
    noinline transformer: Transformer<Out, In>
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

inline infix fun <Event : Any> Store<*, *, Event>.eventsTo(
    eventListener: EventListener<Event>
) = EventListenerConnection(
    eventPublisher = this.eventSource,
    eventListener = eventListener
)