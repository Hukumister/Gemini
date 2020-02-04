package com.haroncode.gemini.binder.dsl

import com.haroncode.gemini.connection.BaseConnectionRule
import com.haroncode.gemini.connection.Transformer
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

@Suppress("NOTHING_TO_INLINE")
inline infix fun <Out : Any, In : Any> Pair<Publisher<Out>, Consumer<In>>.with(
    noinline transformer: Transformer<Out, In>
) = BaseConnectionRule(
    consumer = this.second,
    publisher = this.first,
    transformer = transformer
)

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Any> noneTransformer(): Transformer<T, T> = { input -> input }

@Suppress("NOTHING_TO_INLINE")
inline infix fun <T : Any> BaseConnectionRule<T, T>.decorate(
    noinline transformer: Transformer<T, T>
): BaseConnectionRule<T, T> = BaseConnectionRule(
    consumer = this.consumer,
    publisher = this.publisher,
    transformer = { input ->
        this.transformer(input)
            .compose(transformer)
    }
)