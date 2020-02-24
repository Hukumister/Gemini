package com.haroncode.gemini.android.binder.viewmodel

import androidx.lifecycle.ViewModel
import com.haroncode.gemini.connection.NavigationConnection
import com.haroncode.gemini.connection.dsl.decorate
import com.haroncode.gemini.connection.dsl.noneTransformer
import com.haroncode.gemini.connection.dsl.with
import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.StoreView
import com.haroncode.gemini.core.elements.StoreNavigator
import com.haroncode.gemini.event.EventListener
import com.haroncode.gemini.event.EventListenerConnection
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class ViewModelBinder<Action : Any, State : Any, ViewState : Any, Event : Any>(
    private val store: Store<Action, State, Event>,
    transformer: (State) -> ViewState,
    storeNavigator: StoreNavigator<State, Event>? = null,
    uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel(), ViewBinder<Action, ViewState> {

    private val binderDelegate = ViewModelViewBinder<Action, ViewState> { storeView ->
        add(store to storeView with { input -> input.map(transformer).observeOn(uiScheduler) })
        add(storeView to store with noneTransformer())

        navigatorConnection(store, storeNavigator)?.let { connection ->
            add(connection decorate { stream -> stream.observeOn(uiScheduler) })
        }
        eventListenerConnection(store, storeView)?.let { eventListenerConnection ->
            add(eventListenerConnection decorate { stream -> stream.observeOn(uiScheduler) })
        }
    }

    fun addChildBinder(viewBinder: ViewModelViewBinder<Action, ViewState>) = binderDelegate.addChild(viewBinder)

    override fun bindView(storeView: StoreView<Action, ViewState>) = binderDelegate.bindView(storeView)

    override fun unbindView() = binderDelegate.unbindView()

    override fun onCleared() {
        store.dispose()
        binderDelegate.dispose()
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun ViewModelViewBinder<Action, ViewState>.addToParent() = addChildBinder(this)

    private fun navigatorConnection(
        store: Store<*, State, Event>,
        storeNavigator: StoreNavigator<State, Event>?
    ) = storeNavigator?.let { navigator ->
        NavigationConnection(
            isRetain = true,
            store = store,
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
