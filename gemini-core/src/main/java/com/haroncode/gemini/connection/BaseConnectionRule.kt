package com.haroncode.gemini.connection

import com.haroncode.gemini.core.ConnectionRule
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

/**
 * @author HaronCode.
 */
open class BaseConnectionRule<Out : Any, In : Any>(
    val consumer: Consumer<In>,
    val publisher: Publisher<Out>,
    val transformer: FlowableTransformer<Out, In>
) : ConnectionRule {

    override val isRetain: Boolean = false

    override fun connect(): Disposable = Flowable.fromPublisher(publisher)
        .compose(transformer)
        .subscribe(consumer)
}
