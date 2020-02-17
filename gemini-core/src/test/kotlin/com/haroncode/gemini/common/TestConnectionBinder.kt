package com.haroncode.gemini.common

import com.haroncode.gemini.binder.BaseConnectionBinder
import com.haroncode.gemini.core.ConnectionBinder
import com.haroncode.gemini.core.ConnectionRule
import com.haroncode.gemini.core.StoreLifecycle
import com.haroncode.gemini.core.StoreLifecycle.Event
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber

class TestConnectionBinder : ConnectionBinder {

    private val testLifecycle = TestLifeCycle()
    private val binder = BaseConnectionBinder(testLifecycle)

    fun connectAll() = testLifecycle.connectAll()

    fun disconnectAll() = testLifecycle.disconnectAll()

    override fun bind(connectionRule: ConnectionRule) = binder.bind(connectionRule)

    override fun isDisposed() = binder.isDisposed

    override fun dispose() = binder.dispose()

    private class TestLifeCycle : StoreLifecycle {

        private val testProcessor = PublishProcessor.create<Event>()

        fun connectAll() = testProcessor.onNext(Event.START)

        fun disconnectAll() = testProcessor.onNext(Event.STOP)

        override fun subscribe(subscribers: Subscriber<in Event>) = testProcessor.subscribe(subscribers)
    }
}