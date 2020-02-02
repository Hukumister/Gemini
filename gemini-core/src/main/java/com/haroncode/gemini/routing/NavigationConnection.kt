package com.haroncode.gemini.routing

import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.Flowables
import org.reactivestreams.Publisher
import com.haroncode.gemini.connection.BaseConnectionRule
import com.haroncode.gemini.core.elements.Navigator

class NavigationConnection<State : Any, Event : Any>(
    statePublisher: Publisher<State>,
    eventPublisher: Publisher<Event>,
    private val navigator: Navigator<State, Event>
) : BaseConnectionRule<Pair<State, Event>, Pair<State, Event>>(
    publisher = Flowables.combineLatest(
        Flowable.fromPublisher(statePublisher),
        Flowable.fromPublisher(eventPublisher)
    ),
    consumer = Consumer { (state, event) -> navigator.invoke(state, event) },
    transformer = { input -> input }
)
