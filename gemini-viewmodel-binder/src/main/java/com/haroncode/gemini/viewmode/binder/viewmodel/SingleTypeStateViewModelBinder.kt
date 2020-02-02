package com.haroncode.gemini.viewmode.binder.viewmodel

import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.elements.Navigator

/**
 * @author HaronCode.
 */
abstract class SingleTypeStateViewModelBinder<Action : Any, State : Any, Event : Any>(
        store: Store<Action, State, Event>,
        navigator: Navigator<State, Event>
) : ViewModelBinder<Action, State, State, Event>(
        store = store,
        transformer = { state -> state },
        navigator = navigator
)