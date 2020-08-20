package com.haroncode.gemini.android.connector.dsl

/**
 * @author HaronCode
 * @author kdk96
 */
typealias Mapper<In, Out> = suspend (In) -> Out
