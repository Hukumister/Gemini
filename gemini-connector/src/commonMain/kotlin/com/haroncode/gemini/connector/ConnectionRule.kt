package com.haroncode.gemini.connector

import com.haroncode.gemini.StoreEventListener
import com.haroncode.gemini.StoreView
import com.haroncode.gemini.element.Store
import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

interface ConnectionRule {

    suspend fun connect()

    interface Factory<T> {

        fun create(param: T): ConnectionRule
    }
}

class AutoCancelStoreRule(
    private val store: Store<*, *, *>
) : ConnectionRule {

    fun cancel() = store.coroutineScope.cancel()

    override suspend fun connect() = Unit
}

open class BaseConnectionRule<Out : Any, In : Any>(
    val consumer: Consumer<In>,
    val flow: Flow<Out>,
    val transformer: Transformer<Out, In>
) : ConnectionRule {

    override suspend fun connect() = flow
        .let(transformer::transform)
        .collect { consumer.accept(it) }
}

class ComposeConnectionRule(
    val connectionRules: List<ConnectionRule>
) : ConnectionRule {

    override suspend fun connect() {
        connectionRules.forEach { it.connect() }
    }
}

infix fun <T : Any> Flow<T>.bindTo(
    consumer: Consumer<T>
): BaseConnectionRule<T, T> = BaseConnectionRule(
    consumer = consumer,
    flow = this,
    transformer = identityTransformer()
)

infix fun <Out : Any, In : Any> Flow<Out>.bindTo(
    consumer: Consumer<In>
): Pair<Flow<Out>, Consumer<In>> = this to consumer

infix fun <State : Any> Store<*, State, *>.bindStateTo(
    consumer: Consumer<State>
): BaseConnectionRule<State, State> = stateFlow bindTo consumer

infix fun <State : Any, ViewState : Any> Store<*, State, *>.bindStateTo(
    consumer: Consumer<ViewState>
): Pair<Flow<State>, Consumer<ViewState>> = stateFlow bindTo consumer

infix fun <Action : Any> StoreView<Action, *>.bindActionTo(
    consumer: Consumer<Action>
): BaseConnectionRule<Action, Action> = actionFlow bindTo consumer

infix fun <ViewAction : Any, Action : Any> StoreView<ViewAction, *>.bindActionTo(
    consumer: Consumer<Action>
): Pair<Flow<ViewAction>, Consumer<Action>> = actionFlow bindTo consumer

infix fun <Event : Any> Store<*, *, Event>.bindEventTo(
    eventListener: StoreEventListener<Event>
): BaseConnectionRule<Event, Event> = eventFlow bindTo Consumer(eventListener::onEvent)

infix fun <Event : Any, ViewEvent : Any> Store<*, *, Event>.bindEventTo(
    eventListener: StoreEventListener<ViewEvent>
): Pair<Flow<Event>, Consumer<ViewEvent>> = eventFlow bindTo Consumer(eventListener::onEvent)

infix fun <Out : Any, In : Any> Pair<Flow<Out>, Consumer<In>>.transform(
    transformer: Transformer<Out, In>
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = second,
    flow = first,
    transformer = { flow -> flow.let(transformer::transform) }
)

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

infix fun <Out : Any, In : Any> Pair<Flow<Out>, Consumer<In>>.map(
    mapper: suspend (Out) -> In
): BaseConnectionRule<Out, In> = BaseConnectionRule(
    consumer = second,
    flow = first,
    transformer = { flow -> flow.map(mapper::invoke) }
)
