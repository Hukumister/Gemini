package com.haroncode.gemini.connection

import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.elements.StoreNavigator
import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.Flowables

/**
 * @author HaronCode.
 */
class NavigationConnection<State : Any, Event : Any>(
    override val isRetain: Boolean,
    store: Store<*, State, Event>,
    storeNavigator: StoreNavigator<State, Event>
) : BaseConnectionRule<Pair<State, Event>, Pair<State, Event>>(
    publisher = Flowables.combineLatest(
        Flowable.fromPublisher(store),
        Flowable.fromPublisher(store.eventSource)
    ),
    consumer = Consumer { (state, event) -> storeNavigator.invoke(state, event) },
    transformer = { input -> input }
)
