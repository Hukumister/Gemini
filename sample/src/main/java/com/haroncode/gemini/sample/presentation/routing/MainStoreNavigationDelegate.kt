package com.haroncode.gemini.sample.presentation.routing

import com.haroncode.gemini.core.Store
import com.haroncode.gemini.sample.Screens
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.domain.system.SchedulersProvider
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@PerFragment
class MainStoreNavigationDelegate @Inject constructor(
    mainStore: MainStore,
    schedulersProvider: SchedulersProvider,
    private val router: Router
) : Store<MainStore.Action, Unit, MainStore.Action> by mainStore {

    private val compositeDisposable = CompositeDisposable(mainStore)

    init {
        Flowable.fromPublisher(mainStore.eventSource)
            .observeOn(schedulersProvider.ui())
            .subscribe(::onEvent)
            .addTo(compositeDisposable)
    }

    private fun onEvent(event: MainStore.Action) {
        when (event) {
            MainStore.Action.COUNTER -> router.navigateTo(Screens.Counter)
        }
    }

    override fun dispose() = compositeDisposable.dispose()
}