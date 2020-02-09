package com.haroncode.gemini.binder

import com.haroncode.gemini.core.ConnectionRule
import com.haroncode.gemini.core.StoreLifecycle
import com.haroncode.gemini.core.StoreLifecycle.State
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.reactivestreams.Subscriber
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class BaseConnectionBinderTest {

    private lateinit var testConnectionBinder: BaseConnectionBinder
    private lateinit var testStoreLifecycle: TestStoreLifecycle
    private lateinit var testStoreLifecycleProcessor: FlowableProcessor<State>

    @Before
    fun setUp() {
        testStoreLifecycleProcessor = PublishProcessor.create()
        testStoreLifecycle = TestStoreLifecycle(testStoreLifecycleProcessor)

        testConnectionBinder = BaseConnectionBinder(testStoreLifecycle)
    }

    @Test
    fun `binder subscribe if lifecycle is active`() {
        val connectionRule = TestConnectionRule(false)

        testStoreLifecycleProcessor.onNext(State.START)
        testConnectionBinder.bind(connectionRule)

        Assert.assertTrue(connectionRule.isConnected.get())
    }

    @Test
    fun `binder doesn't subscribe if lifecycle is active`() {
        val connectionRule = TestConnectionRule(false)

        testStoreLifecycleProcessor.onNext(State.STOP)
        testConnectionBinder.bind(connectionRule)

        Assert.assertFalse(connectionRule.isConnected.get())
    }

    @Test
    fun `binder doesn't dispose connection if it's retain connection`() {
        val connectionRule = TestConnectionRule(true)

        testStoreLifecycleProcessor.onNext(State.START)
        testConnectionBinder.bind(connectionRule)
        testStoreLifecycleProcessor.onNext(State.STOP)

        Assert.assertTrue(connectionRule.isConnected.get())
    }

    @Test
    fun `binder dispose all connection if event stop`() {
        val connections = listOf(
            TestConnectionRule(false),
            TestConnectionRule(false),
            TestConnectionRule(false)
        )

        testStoreLifecycleProcessor.onNext(State.START)
        connections.forEach(testConnectionBinder::bind)
        testStoreLifecycleProcessor.onNext(State.STOP)

        val allDisconnected = connections.all { connection -> !connection.isConnected.get() }
        Assert.assertTrue(allDisconnected)
    }

    @Test
    fun `binder dispose all connection if binder is disposed`() {
        val connections = listOf(
            TestConnectionRule(false),
            TestConnectionRule(false),
            TestConnectionRule(true)
        )

        testStoreLifecycleProcessor.onNext(State.START)
        connections.forEach(testConnectionBinder::bind)
        testConnectionBinder.dispose()

        val allDisconnected = connections.all { connection -> !connection.isConnected.get() }
        Assert.assertTrue(allDisconnected)
    }

    @Test
    fun `binder connect again all connections after stop event`() {
        val connections = listOf(
            TestConnectionRule(false),
            TestConnectionRule(false),
            TestConnectionRule(true)
        )

        testStoreLifecycleProcessor.onNext(State.START)
        connections.forEach(testConnectionBinder::bind)
        testStoreLifecycleProcessor.onNext(State.STOP)

        testStoreLifecycleProcessor.onNext(State.START)

        val allConnected = connections.all { connection -> connection.isConnected.get() }
        Assert.assertTrue(allConnected)
    }

    @Test
    fun `binder distinct events`() {
        val connectionRule = TestConnectionRule(true)

        testStoreLifecycleProcessor.onNext(State.START)
        testConnectionBinder.bind(connectionRule)
        testStoreLifecycleProcessor.onNext(State.STOP)

        testStoreLifecycleProcessor.onNext(State.START)
        testStoreLifecycleProcessor.onNext(State.START)
        testStoreLifecycleProcessor.onNext(State.START)

        val countConnection = connectionRule.countConnection.get()
        Assert.assertEquals(countConnection, 1)
    }

    @Test
    fun `correct work isDisposed method`() {
        val beforeDispose = testConnectionBinder.isDisposed

        testConnectionBinder.dispose()

        Assert.assertFalse(beforeDispose)
        Assert.assertTrue(testConnectionBinder.isDisposed)
    }

    class TestStoreLifecycle(
        private val testProcessor: FlowableProcessor<State>
    ) : StoreLifecycle {

        override fun subscribe(subscriber: Subscriber<in State>) = testProcessor.subscribe(subscriber)
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