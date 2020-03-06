package com.haroncode.gemini.connection.util

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

typealias Transformer<In, Out> = (Flowable<In>) -> Flowable<Out>

private val IDENTITY_TRANSFORMER: Transformer<Any, Any> = { stream -> stream }

private val IDENTITY_FLOWABLE_TRANSFORMER = FlowableTransformer<Any, Any> { stream -> stream }

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityFlowableTransformer() = IDENTITY_FLOWABLE_TRANSFORMER as FlowableTransformer<T, T>

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityTransformer() = IDENTITY_TRANSFORMER as Transformer<T, T>
