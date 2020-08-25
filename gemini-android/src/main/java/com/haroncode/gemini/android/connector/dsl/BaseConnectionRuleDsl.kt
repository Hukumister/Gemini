@file:Suppress("NOTHING_TO_INLINE")

package com.haroncode.gemini.android.connector.dsl

import com.haroncode.gemini.StoreEventListener
import com.haroncode.gemini.StoreView
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.Transformer
import com.haroncode.gemini.connector.identityTransformer
import com.haroncode.gemini.element.Store
import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author HaronCode
 * @author kdk96
 */
inline infix fun <T : Any, R : Any> Flow<T>.connectTo(
    consumer: Consumer<R>
): Pair<Flow<T>, Consumer<R>> = this to consumer

inline infix fun <T : Any> Flow<T>.connectTo(
    consumer: Consumer<T>
): BaseConnectionRule<T, T> = BaseConnectionRule(
    consumer = consumer,
    flow = this,
    transformer = identityTransformer()
)

inline infix fun <State : Any> Store<*, State, *>.stateTo(
    consumer: Consumer<State>
): BaseConnectionRule<State, State> = stateFlow connectTo consumer

inline infix fun <Action : Any> StoreView<*, Action>.actionTo(
    consumer: Consumer<Action>
): BaseConnectionRule<Action, Action> = actionFlow connectTo consumer

inline infix fun <Event : Any> Store<*, *, Event>.eventTo(
    eventListener: StoreEventListener<Event>
): BaseConnectionRule<Event, Event> = eventFlow connectTo eventListener::onEvent

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
