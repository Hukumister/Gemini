package com.haroncode.gemini.keeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.haroncode.gemini.element.Store
import kotlinx.coroutines.cancel

inline fun <reified T, Action : Any, State : Any, Event : Any> ViewModelStoreOwner.getStore(
    noinline provider: () -> T
) where T : Store<Action, State, Event> =
    getStore(T::class.java.canonicalName!!, provider)

fun <T, Action : Any, State : Any, Event : Any> ViewModelStoreOwner.getStore(
    key: String,
    provider: () -> T
) where T : Store<Action, State, Event> =
    ViewModelProvider(this)
        .get(StoreKeeperViewModel::class.java)
        .get(key, provider)

internal class StoreKeeperViewModel : ViewModel() {

    private val stores = HashMap<String, Store<*, *, *>>()

    @Suppress("UNCHECKED_CAST")
    fun <T, Action : Any, State : Any, Event : Any> get(
        key: String,
        provider: () -> T
    ) where T : Store<Action, State, Event> =
        stores.getOrPut(key, provider) as T

    override fun onCleared() {
        stores.values.forEach { store -> store.coroutineScope.cancel() }
        stores.clear()
    }
}
