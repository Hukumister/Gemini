package com.haroncode.gemini.viewmode.binder.viewmodel

import androidx.lifecycle.ViewModel
import com.haroncode.gemini.binder.ConnectionViewBinder
import com.haroncode.gemini.binder.ViewBinder
import com.haroncode.gemini.binder.dsl.*
import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.StoreView
import com.haroncode.gemini.core.elements.Navigator
import com.haroncode.gemini.routing.EventListener
import com.haroncode.gemini.routing.EventListenerConnection
import com.haroncode.gemini.routing.NavigationConnection
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class ViewModelBinder<Action : Any, State : Any, ViewState : Any, Event : Any>(
        private val store: Store<Action, State, Event>,
        navigator: Navigator<State, Event>,
        transformer: (State) -> ViewState,
        uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel(), ViewBinder<Action, ViewState> {

    private val binderDelegate = binding<Action, ViewState> { storeView ->

        connection { store to storeView with { input -> input.map(transformer).observeOn(uiScheduler) } }
        connection { storeView to store with noneTransformer() }

        val navigationConnection = NavigationConnection(store, store.eventSource, navigator)
        connection { navigationConnection decorate { stream -> stream.observeOn(uiScheduler) } }

        @Suppress("UNCHECKED_CAST")
        (storeView as? EventListener<Event>)
                ?.let { listener -> eventListenerConnection(store, listener) }
                ?.let { eventListenerConnection ->
                    connection {
                        eventListenerConnection decorate { stream -> stream.observeOn(uiScheduler) }
                    }
                }
    }.apply { childBinding?.let(::addChild) }

    protected open val childBinding: ConnectionViewBinder<Action, ViewState>? = null

    override fun bindView(storeView: StoreView<Action, ViewState>) = binderDelegate.bindView(storeView)

    override fun unbindView() = binderDelegate.unbindView()

    private fun <Event : Any> eventListenerConnection(
            store: Store<*, *, Event>,
            eventListener: EventListener<Event>
    ) = EventListenerConnection(
            eventPublisher = store.eventSource,
            eventListener = eventListener
    )

    override fun onCleared() = store.dispose()

}