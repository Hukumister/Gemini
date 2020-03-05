package com.haroncode.gemini.connector

import com.haroncode.gemini.connection.ConnectionRule
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BaseStoreConnectorTest {

    private lateinit var testConnector: StoreConnector

    private lateinit var testConnectionRule: TestConnectionRule
    private lateinit var testRetainConnectionRule: TestConnectionRule

    @Before
    fun setUp() {
        testConnectionRule = TestConnectionRule(isRetain = false)
        testRetainConnectionRule = TestConnectionRule(isRetain = true)

        testConnector = BaseStoreConnector(listOf(testConnectionRule, testRetainConnectionRule))
    }

    @Test
    fun `connector connect correctly`() {
        testConnector.connect()

        assertTrue(testConnectionRule.isConnected.get())
    }

    @Test
    fun `connector doesn't dispose connection if it's retain connection`() {
        testConnector.connect()
        testConnector.disconnect()

        assertTrue(testRetainConnectionRule.isConnected.get())
        assertFalse(testConnectionRule.isConnected.get())
    }

    @Test
    fun `connector dispose connection correctly`() {
        testConnector.connect()
        testConnector.dispose()

        assertFalse(testRetainConnectionRule.isConnected.get())
        assertFalse(testConnectionRule.isConnected.get())
    }

    @Test
    fun `connector connects again all connections after disconnect`() {
        testConnector.connect()
        testConnector.disconnect()

        testConnector.connect()

        assertTrue(testConnectionRule.isConnected.get())
    }

    @Test
    fun `connector isDispose method works correctly`() {
        testConnector.dispose()

        assertTrue(testConnector.isDisposed)
    }

    @Test
    fun `retain connections aren't connected twice`() {
        testConnector.connect()
        testConnector.connect()

        assertEquals(testRetainConnectionRule.countConnection.get(), 1)
    }

    class TestConnectionRule(
        override val isRetain: Boolean,
        private val testSubject: Subject<Unit> = PublishSubject.create()
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
