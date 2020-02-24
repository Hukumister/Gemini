package com.haroncode.gemini.android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

class RxLifecycleEventObserver : Publisher<Lifecycle.Event>, LifecycleEventObserver {

    private val eventProcessor = PublishProcessor.create<Lifecycle.Event>()

    override fun subscribe(subscriber: Subscriber<in Lifecycle.Event>) = eventProcessor.subscribe(subscriber)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) = eventProcessor.onNext(event)
}
