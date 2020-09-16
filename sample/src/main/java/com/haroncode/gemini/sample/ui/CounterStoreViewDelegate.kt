package com.haroncode.gemini.sample.ui

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.StoreViewDelegate
import com.haroncode.gemini.functional.Consumer
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore

class CounterStoreViewDelegate(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    stateConsumer: Consumer<CounterStore.State>
) : StoreViewDelegate<CounterStore.Action, CounterStore.State>(
    savedStateRegistryOwner,
    stateConsumer
)
