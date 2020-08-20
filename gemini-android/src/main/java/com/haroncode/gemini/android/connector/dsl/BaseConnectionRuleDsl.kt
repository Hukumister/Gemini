@file:Suppress("NOTHING_TO_INLINE")

package com.haroncode.gemini.android.connector.dsl

import com.haroncode.gemini.StoreEventListener
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.Transformer
import com.haroncode.gemini.connector.identityTransformer
import com.haroncode.gemini.element.Store
import com.haroncode.gemini.util.Consumer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author HaronCode
 * @author kdk96
 */
inline infix fun <T : Any, R : Any> Flow<T>.connectTo(
    noinline consumer: Consumer<R>
): Pair<Flow<T>, Consumer<R>> = this to consumer

inline infix fun <State : Any> Store<*, State, *>.stateTo(
    noinline consumer: Consumer<State>
): BaseConnectionRule<State, State> = BaseConnectionRule(
    consumer = consumer,
    flow = stateFlow,
    transformer = identityTransformer()
)

inline infix fun <Event : Any> Store<*, *, Event>.eventTo(
    eventListener: StoreEventListener<Event>
): BaseConnectionRule<Event, Event> = BaseConnectionRule(
    consumer = eventListener::onEvent,
    flow = eventFlow,
    transformer = identityTransformer()
)

inline infix fun <Out : Any, In : Any> BaseConnectionRule<Out, In>.transform(
    crossinline transformer: Transformer<In, In>
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = consumer,
    flow = flow,
    transformer = { flow ->
        flow
            .let(this.transformer)
            .let(transformer)
    }
)

inline infix fun <Out : Any, In : Any> Pair<Flow<Out>, Consumer<In>>.transform(
    crossinline transformer: Transformer<Out, In>
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = second,
    flow = first,
    transformer = { flow -> flow.let(transformer) }
)

inline infix fun <Out : Any, In : Any> Pair<Flow<Out>, Consumer<In>>.map(
    crossinline mapper: Mapper<Out, In>
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = second,
    flow = first,
    transformer = { flow -> flow.map(mapper) }
)
