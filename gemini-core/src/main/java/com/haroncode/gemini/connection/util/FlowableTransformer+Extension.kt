package com.haroncode.gemini.connection.util

import io.reactivex.FlowableTransformer

private val IDENTITY_FLOWABLE_TRANSFORMER = FlowableTransformer<Any, Any> { stream -> stream }

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityFlowableTransformer() = IDENTITY_FLOWABLE_TRANSFORMER as FlowableTransformer<T, T>
