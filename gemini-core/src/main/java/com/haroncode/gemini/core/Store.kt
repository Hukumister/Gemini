package com.haroncode.gemini.core

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

/**
 * @author HaronCode.
 */
interface Store<Action : Any, State : Any, Event : Any> : Consumer<Action>, Publisher<State>, Disposable {

    val eventSource: Publisher<Event>
}