package com.haroncode.gemini.sample.ui

import androidx.lifecycle.ViewModelStoreOwner
import com.haroncode.gemini.StoreViewDelegate
import com.haroncode.gemini.functional.Consumer
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore

class CounterStoreViewDelegate(
    counterFragment: CounterFragment,
    stateConsumer: Consumer<CounterStore.State>
) : StoreViewDelegate<CounterStore.Action, CounterStore.State>(
    counterFragment,
    stateConsumer
),
    ViewModelStoreOwner by counterFragment
