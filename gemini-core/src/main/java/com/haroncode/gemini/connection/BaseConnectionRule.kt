package com.haroncode.gemini.connection

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

typealias Transformer<In, Out> = (Flowable<In>) -> Flowable<Out>

open class BaseConnectionRule<Out : Any, In : Any>(
    val consumer: Consumer<In>,
    val publisher: Publisher<Out>,
    val transformer: Transformer<Out, In>
) : ConnectionRule {

    override fun connect(): Disposable = Flowable.fromPublisher(publisher)
        .compose(transformer)
        .subscribe(consumer)

}