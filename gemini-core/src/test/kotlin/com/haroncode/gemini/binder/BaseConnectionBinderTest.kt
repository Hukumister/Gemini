package com.haroncode.gemini.binder

import com.haroncode.gemini.core.ConnectionRule
import com.haroncode.gemini.core.StoreLifecycle
import com.haroncode.gemini.core.StoreLifecycle.Event
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.reactivestreams.Subscriber

class BaseConnectionBinderTest {

    private lateinit var testConnectionBinder: BaseConnectionBinder
    private lateinit var testStoreLifecycle: TestStoreLifecycle
    private lateinit var testStoreLifecycleProcessor: FlowableProcessor<Event>

    @Before
    fun setUp() {
        testStoreLifecycleProcessor = PublishProcessor.create()
        testStoreLifecycle = TestStoreLifecycle(testStoreLifecycleProcessor)

        testConnectionBinder = BaseConnectionBinder(testStoreLifecycle)
    }

    @Test
    fun `binder subscribes if lifecycle is active`() {
        val connectionRule = TestConnectionRule(false)

        testStoreLifecycleProcessor.onNext(Event.START)
        testConnectionBinder.bind(connectionRule)

        assertTrue(connectionRule.isConnected.get())
    }

    @Test
    fun `binder doesn't subscribe if lifecycle is active`() {
        val connectionRule = TestConnectionRule(false)

        testStoreLifecycleProcessor.onNext(Event.STOP)
        testConnectionBinder.bind(connectionRule)

        assertFalse(connectionRule.isConnected.get())
    }

    @Test
    fun `binder doesn't dispose connection if it's retain connection`() {
        val connectionRule = TestConnectionRule(true)

        testStoreLifecycleProcessor.onNext(Event.START)
        testConnectionBinder.bind(connectionRule)
        testStoreLifecycleProcessor.onNext(Event.STOP)

        assertTrue(connectionRule.isConnected.get())
    }

    @Test
    fun `binder disposes all connection if event stop`() {
        val connections = listOf(
            TestConnectionRule(false),
            TestConnectionRule(false),
            TestConnectionRule(false)
        )

        testStoreLifecycleProcessor.onNext(Event.START)
        connections.forEach(testConnectionBinder::bind)
        testStoreLifecycleProcessor.onNext(Event.STOP)

        val allDisconnected = connections.all { connection -> !connection.isConnected.get() }
        assertTrue(allDisconnected)
    }

    @Test
    fun `binder disposes all connection if binder is disposed`() {
        val connections = listOf(
            TestConnectionRule(false),
            TestConnectionRule(false),
            TestConnectionRule(true)
        )

        testStoreLifecycleProcessor.onNext(Event.START)
        connections.forEach(testConnectionBinder::bind)
        testConnectionBinder.dispose()

        val allDisconnected = connections.all { connection -> !connection.isConnected.get() }
        assertTrue(allDisconnected)
    }

    @Test
    fun `binder connects again all connections after stop event`() {
        val connections = listOf(
            TestConnectionRule(false),
            TestConnectionRule(false),
            TestConnectionRule(true)
        )

        testStoreLifecycleProcessor.onNext(Event.START)
        connections.forEach(testConnectionBinder::bind)
        testStoreLifecycleProcessor.onNext(Event.STOP)

        testStoreLifecycleProcessor.onNext(Event.START)

        val allConnected = connections.all { connection -> connection.isConnected.get() }
        assertTrue(allConnected)
    }

    @Test
    fun `binder doesn't react until changed events`() {
        val connectionRule = TestConnectionRule(true)

        testStoreLifecycleProcessor.onNext(Event.START)
        testConnectionBinder.bind(connectionRule)
        testStoreLifecycleProcessor.onNext(Event.STOP)

        testStoreLifecycleProcessor.onNext(Event.START)
        testStoreLifecycleProcessor.onNext(Event.START)
        testStoreLifecycleProcessor.onNext(Event.START)

        val countConnection = connectionRule.countConnection.get()
        Assert.assertEquals(countConnection, 1)
    }

    @Test
    fun `method isDisposed correct works`() {
        val beforeDispose = testConnectionBinder.isDisposed

        testConnectionBinder.dispose()

        assertFalse(beforeDispose)
        assertTrue(testConnectionBinder.isDisposed)
    }

    class TestStoreLifecycle(
        private val testProcessor: FlowableProcessor<Event>
    ) : StoreLifecycle {

        override fun subscribe(subscriber: Subscriber<in Event>) = testProcessor.subscribe(subscriber)
    }

    class TestConnectionRule(
        override val isRetain: Boolean,
        val testSubject: Subject<Unit> = PublishSubject.create()
    ) : ConnectionRule {

        val countConnection = AtomicInteger(0)
        var isConnected = AtomicBoolean(false)

        override fun connect(): Disposable = testSubject
            .doOnSubscribe {
                countConnection.incrementAndGet()
                isConnected.set(true)
            }
            .doOnDispose {
                countConnection.decrementAndGet()
                isConnected.set(false)
            }
            .subscribe()
    }
}
