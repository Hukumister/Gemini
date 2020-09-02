package com.haroncode.gemini.binder

import com.haroncode.gemini.StoreEventListener
import com.haroncode.gemini.StoreView
import com.haroncode.gemini.element.Store
import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*

sealed class BindingRule

class AutoCancelStoreRule(
    private val store: Store<*, *, *>
) : BindingRule() {

    fun cancel() = store.coroutineScope.cancel()
}

open class BaseBindingRule<Out : Any, In : Any>(
    val consumer: Consumer<In>,
    val flow: Flow<Out>,
    val transformer: Transformer<Out, In>
) : BindingRule() {

    suspend fun bind() = flow
        .let(transformer::transform)
        .collect { consumer.accept(it) }
}

infix fun <T : Any> Flow<T>.bindTo(
    consumer: Consumer<T>
): BaseBindingRule<T, T> = BaseBindingRule(
    consumer = consumer,
    flow = this,
    transformer = identityTransformer()
)

infix fun <Out : Any, In : Any> Flow<Out>.bindTo(
    consumer: Consumer<In>
): Pair<Flow<Out>, Consumer<In>> = this to consumer

infix fun <State : Any> Store<*, State, *>.bindStateTo(
    consumer: Consumer<State>
): BaseBindingRule<State, State> = stateFlow bindTo consumer

infix fun <State : Any, ViewState : Any> Store<*, State, *>.bindStateTo(
    consumer: Consumer<ViewState>
): Pair<Flow<State>, Consumer<ViewState>> = stateFlow bindTo consumer

infix fun <Action : Any> StoreView<Action, *>.bindActionTo(
    consumer: Consumer<Action>
): BaseBindingRule<Action, Action> = actionFlow bindTo consumer

infix fun <ViewAction : Any, Action : Any> StoreView<ViewAction, *>.bindActionTo(
    consumer: Consumer<Action>
): Pair<Flow<ViewAction>, Consumer<Action>> = actionFlow bindTo consumer

infix fun <Event : Any> Store<*, *, Event>.bindEventTo(
    eventListener: StoreEventListener<Event>
): BaseBindingRule<Event, Event> = eventFlow bindTo Consumer(eventListener::onEvent)

infix fun <Event : Any, ViewEvent : Any> Store<*, *, Event>.bindEventTo(
    eventListener: StoreEventListener<ViewEvent>
): Pair<Flow<Event>, Consumer<ViewEvent>> = eventFlow bindTo Consumer(eventListener::onEvent)

infix fun <Out : Any, In : Any> Pair<Flow<Out>, Consumer<In>>.transform(
    transformer: Transformer<Out, In>
): BaseBindingRule<Out, In> = BaseBindingRule(
    consumer = second,
    flow = first,
    transformer = { flow -> flow.let(transformer::transform) }
)

infix fun <Out : Any, In : Any> BaseBindingRule<Out, In>.transform(
    outputTransformer: Transformer<In, In>
): BaseBindingRule<Out, In> = BaseBindingRule(
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
): BaseBindingRule<Out, In> = BaseBindingRule(
    consumer = second,
    flow = first,
    transformer = { flow -> flow.map(mapper::invoke) }
)
