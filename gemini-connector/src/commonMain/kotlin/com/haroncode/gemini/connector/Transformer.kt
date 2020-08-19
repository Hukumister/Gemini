package com.haroncode.gemini.connector

import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
typealias Transformer<In, Out> = (Flow<In>) -> Flow<Out>

private val IDENTITY_TRANSFORMER: Transformer<Any, Any> = { flow -> flow }

@Suppress("UNCHECKED_CAST")
fun <T : Any> identityTransformer() = IDENTITY_TRANSFORMER as Transformer<T, T>
