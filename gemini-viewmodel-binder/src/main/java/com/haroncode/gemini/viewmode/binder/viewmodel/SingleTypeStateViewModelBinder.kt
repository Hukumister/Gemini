package com.haroncode.gemini.viewmode.binder.viewmodel

import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.elements.StoreNavigator

/**
 * @author HaronCode.
 */
abstract class SingleTypeStateViewModelBinder<Action : Any, State : Any, Event : Any>(
    store: Store<Action, State, Event>,
    storeNavigator: StoreNavigator<State, Event>? = null
) : ViewModelBinder<Action, State, State, Event>(
    store = store,
    transformer = { state -> state },
    storeNavigator = storeNavigator
)