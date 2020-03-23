@file:Suppress("NOTHING_TO_INLINE")

package com.haroncode.gemini.sample.util

import com.haroncode.gemini.sample.domain.model.Resource
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.annotations.BackpressureKind
import io.reactivex.annotations.BackpressureSupport
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun Navigator.setLaunchScreen(screen: SupportAppScreen) {
    applyCommands(
        arrayOf(
            BackTo(null),
            Replace(screen)
        )
    )
}

/**
 * Wrap T to Product<T>
 * Flowable<T> -> Flowable<Product<T>>
 */
@CheckReturnValue
@BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <T> Single<T>.asResource(): Flowable<Resource<T>> = map<Resource<T>> { data -> Resource.Data(data) }
    .toFlowable()
    .onErrorReturn { throwable -> Resource.Error(throwable) }
    .startWith(Resource.Loading)

inline fun <T> T.asFlowable() = Flowable.just(this)