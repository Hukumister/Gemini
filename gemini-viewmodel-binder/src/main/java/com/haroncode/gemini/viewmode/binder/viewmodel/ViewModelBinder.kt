package com.haroncode.gemini.viewmode.binder.viewmodel

import androidx.lifecycle.ViewModel
import com.haroncode.gemini.binder.BinderComposer
import com.haroncode.gemini.binder.ViewBinder
import com.haroncode.gemini.binder.dsl.*
import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.StoreView
import com.haroncode.gemini.core.elements.StoreNavigator
import com.haroncode.gemini.routing.EventListener
import com.haroncode.gemini.connection.EventListenerConnection
import com.haroncode.gemini.connection.NavigationConnection
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class ViewModelBinder<Action : Any, State : Any, ViewState : Any, Event : Any>(
    private val store: Store<Action, State, Event>,
    transformer: (State) -> ViewState,
    storeNavigator: StoreNavigator<State, Event>? = null,
    uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel(), ViewBinder<Action, ViewState> {

    private val parentBinder = binding<Action, ViewState> { storeView ->
        connection { store to storeView with { input -> input.map(transformer).observeOn(uiScheduler) } }
        connection { storeView to store with noneTransformer() }

        navigatorConnection(store, storeNavigator)
            ?.let { connection -> connection { connection decorate { stream -> stream.observeOn(uiScheduler) } } }

        eventListenerConnection(store, storeView)?.let { eventListenerConnection ->
            connection { eventListenerConnection decorate { stream -> stream.observeOn(uiScheduler) } }
        }
    }

    private val binderDelegate = BinderComposer(parentBinder)

    override fun bindView(storeView: StoreView<Action, ViewState>) = binderDelegate.bindView(storeView)

    override fun unbindView() = binderDelegate.unbindView()

    fun addChildBinder(viewBinder: ViewBinder<Action, ViewState>) = binderDelegate.addBinder(viewBinder)

    override fun onCleared() = store.dispose()

    @Suppress("NOTHING_TO_INLINE")
    inline fun ViewBinder<Action, ViewState>.addToParent() = addChildBinder(this)

    private fun navigatorConnection(
        store: Store<*, State, Event>,
        storeNavigator: StoreNavigator<State, Event>?
    ) = storeNavigator?.let { navigator ->
        NavigationConnection(
            statePublisher = store,
            eventPublisher = store.eventSource,
            storeNavigator = navigator
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun eventListenerConnection(
        store: Store<*, *, Event>,
        storeView: StoreView<Action, ViewState>
    ) = (storeView as? EventListener<Event>)?.let { listener ->
        EventListenerConnection(
            eventPublisher = store.eventSource,
            eventListener = listener
        )
    }
}