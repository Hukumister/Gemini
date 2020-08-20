@file:Suppress("NOTHING_TO_INLINE")

package com.haroncode.gemini.sample.util

import com.haroncode.gemini.sample.domain.model.Resource
import io.reactivex.annotations.BackpressureKind
import io.reactivex.annotations.BackpressureSupport
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
inline fun <T> Flow<T>.asResource(): Flow<Resource<T>> = map<T, Resource<T>> { data -> Resource.Data(data) }
    .catch { throwable -> emit(Resource.Error(throwable)) }
    .onStart { emit(Resource.Loading) }
