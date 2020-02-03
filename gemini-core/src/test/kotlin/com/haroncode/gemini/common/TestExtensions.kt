package com.haroncode.gemini.common

import io.reactivex.observers.TestObserver

@Suppress("UNCHECKED_CAST")
fun <T> TestObserver<T>.onNextEvents() = events.first() as List<T>
