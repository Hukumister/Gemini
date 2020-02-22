package com.haroncode.gemini.common

import io.reactivex.observers.TestObserver

fun <T> TestObserver<T>.onNextEvents() = events.first() as List<T>
