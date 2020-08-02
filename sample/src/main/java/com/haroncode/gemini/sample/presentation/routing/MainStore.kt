package com.haroncode.gemini.sample.presentation.routing

import com.haroncode.gemini.sample.Screens
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.presentation.routing.MainStore.Action
import com.haroncode.gemini.store.OnlyActionStore
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import ru.terrakok.cicerone.Router

@PerFragment
class MainStore @Inject constructor(
    private val router: Router
) : OnlyActionStore<Action, Unit, Action>(
    initialState = Unit,
    reducer = { _, _ -> Unit },
    middleware = { action, _ -> Flowable.just(action) },
    eventProducer = { _, action -> action }
) {

    enum class Action {
        COUNTER,
        AUTH,
    }

    init {
        Flowable.fromPublisher(eventSource)
            .subscribeBy { event ->
                when (event) {
                    Action.COUNTER -> router.navigateTo(Screens.Counter)
                    Action.AUTH -> router.navigateTo(Screens.Auth)
                }
            }
            .autoDispose()
    }

    private fun Disposable.autoDispose() = add(this)
}
