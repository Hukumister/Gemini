package com.haroncode.gemini.dsl

import io.reactivex.Flowable

typealias Transformer<In, Out> = (Flowable<In>) -> Flowable<Out>

private val IDENTITY_TRANSFORMER: Transformer<Any, Any> = { stream -> stream }

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityTransformer() = IDENTITY_TRANSFORMER as Transformer<T, T>
