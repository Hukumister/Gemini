package com.haroncode.gemini

import com.haroncode.gemini.TestAction.ActionForEvent
import com.haroncode.gemini.TestAction.FulfillableAsync
import com.haroncode.gemini.TestAction.FulfillableInstantly
import com.haroncode.gemini.TestAction.LeadsToExceptionInMiddleware
import com.haroncode.gemini.TestAction.MaybeFulfillable
import com.haroncode.gemini.TestAction.TranslatesTo3Effects
import com.haroncode.gemini.TestAction.TranslatesToExceptionInReducer
import com.haroncode.gemini.TestAction.Unfulfillable
import com.haroncode.gemini.store.BaseStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BaseStoreTest {

    private lateinit var testDispatcher: TestCoroutineDispatcher
    private lateinit var testBootstrapperChannel: BroadcastChannel<TestAction>
    private lateinit var testErrorHandler: TestErrorHandler<TestState>
    private lateinit var baseStore: BaseStore<TestAction, TestState, TestEvent, TestEffect>

    private lateinit var testActionChannel: BroadcastChannel<TestAction>
    private lateinit var testStates: MutableList<TestState>

    private lateinit var stateJob: Job

    @BeforeTest
    fun setup() {
        testDispatcher = TestCoroutineDispatcher()
        testBootstrapperChannel = BroadcastChannel(BUFFERED)
        testErrorHandler = TestErrorHandler()
        baseStore = BaseStore(
            initialState = TestState(),
            middleware = TestMiddleware(testDispatcher),
            reducer = TestReducer(),
            bootstrapper = TestBootstrapper(testBootstrapperChannel.asFlow()),
            eventProducer = TestEventProducer(),
            errorHandler = testErrorHandler,
            coroutineDispatcher = testDispatcher
        )

        testActionChannel = BroadcastChannel(BUFFERED)
        testStates = mutableListOf()

        baseStore.coroutineScope.launch {
            launch {
                testActionChannel.asFlow()
                    .collect(baseStore::accept)
            }

            stateJob = launch {
                baseStore.stateFlow
                    .collect(testStates::add)
            }
        }
    }

    @Test
    fun `if there are no actions, store only emits initial state`() {
        assertEquals(1, testStates.size)
    }

    @Test
    fun `emitted initial state is correct`() {
        val state = testStates.last()
        assertEquals(INITIAL_COUNTER, state.counter)
        assertEquals(INITIAL_LOADING, state.loading)
    }

    @Test
    fun `bootstrapper works correctly`() = runBlockingTest {
        val actions = listOf(
            Unfulfillable,
            Unfulfillable,
            FulfillableInstantly
        )

        actions.forEach { testBootstrapperChannel.send(it) }

        assertEquals(2, testStates.size)
    }

    @Test
    fun `there should be no state emission besides the initial one for unfulfillable actions`() = runBlockingTest {
        val actions = listOf(
            Unfulfillable,
            Unfulfillable,
            Unfulfillable
        )

        actions.forEach { testActionChannel.send(it) }

        assertEquals(1, testStates.size)
    }

    @Test
    fun `there should be the same amount of states as actions that translate 1 - 1 to effects plus one for initial state`() =
        runBlockingTest {
            val actions = listOf(
                FulfillableInstantly,
                FulfillableInstantly,
                FulfillableInstantly
            )

            actions.forEach { testActionChannel.send(it) }

            assertEquals(1 + actions.size, testStates.size)
        }

    @Test
    fun `there should be 3 times as many states as actions that translate 1 - 3 to effects plus one for initial state`() =
        runBlockingTest {
            val actions = listOf(
                TranslatesTo3Effects,
                TranslatesTo3Effects,
                TranslatesTo3Effects
            )

            actions.forEach { testActionChannel.send(it) }

            assertEquals(1 + actions.size * 3, testStates.size)
        }

    @Test
    fun `the number of state emissions should reflect the number of effects plus one for initial state in complex case`() =
        runBlockingTest {
            val actions = listOf(
                FulfillableInstantly, // maps to 1 effect
                FulfillableInstantly, // maps to 1 effect
                MaybeFulfillable, // maps to 1 in this case
                Unfulfillable, // maps to 0
                FulfillableInstantly, // maps to 1
                FulfillableInstantly, // maps to 1
                MaybeFulfillable, // maps to 0 in this case
                TranslatesTo3Effects // maps to 3
            )

            actions.forEach { testActionChannel.send(it) }

            assertEquals(8 + 1, testStates.size)
            val expectedState = TestState(
                counter = (INITIAL_COUNTER + 1 + 1) * 10 + 1 + 1 + 3 * 1,
                loading = false
            )
            assertEquals(expectedState, testStates.last())
        }

    @Test
    fun `there should be no state emission after store cancellation`() = runBlockingTest {
        val mockServerDelayMs = 10L

        testActionChannel.send(FulfillableAsync(mockServerDelayMs))

        assertEquals(2, testStates.size)

        val stateBeforeCancel = testStates.last()

        assertEquals(INITIAL_COUNTER, stateBeforeCancel.counter)
        assertEquals(true, stateBeforeCancel.loading)

        testDispatcher.advanceTimeBy(5)

        baseStore.coroutineScope.cancel()

        testDispatcher.advanceTimeBy(10)

        val stateAfterCancel = testStates.last()

        assertEquals(INITIAL_COUNTER, stateAfterCancel.counter)
        assertEquals(true, stateAfterCancel.loading)
    }

    @Test
    fun `last state should be delivered after reconnect`() = runBlockingTest {
        val mockServerDelayMs = 10L

        testActionChannel.send(FulfillableAsync(mockServerDelayMs))

        assertEquals(2, testStates.size)

        val stateBeforeJobCancel = testStates.last()

        assertEquals(INITIAL_COUNTER, stateBeforeJobCancel.counter)
        assertEquals(true, stateBeforeJobCancel.loading)

        testDispatcher.advanceTimeBy(5)

        stateJob.cancel()

        testDispatcher.advanceTimeBy(10)

        stateJob = launch {
            baseStore.stateFlow
                .collect { testStates.add(it) }
        }

        val stateAfterReconnect = testStates.last()

        stateJob.cancel()

        assertEquals(INITIAL_COUNTER + DELAYED_FULFILL_AMOUNT, stateAfterReconnect.counter)
        assertEquals(false, stateAfterReconnect.loading)
    }

    @Test
    fun `error handler handles exception in middleware`() = runBlockingTest {
        testActionChannel.send(FulfillableInstantly)

        val expectedState = TestState(
            counter = INITIAL_COUNTER + 1,
            loading = false
        )

        assertEquals(expectedState, testStates.last())

        testActionChannel.send(LeadsToExceptionInMiddleware)

        assertEquals(expectedState, testErrorHandler.lastState)
        assertTrue(testErrorHandler.lastThrowable is Exception)

        testActionChannel.send(FulfillableInstantly)

        val expectedStateAfterErrorHandling = expectedState.copy(counter = expectedState.counter + 1)

        assertEquals(expectedStateAfterErrorHandling, testStates.last())
    }

    @Test
    fun `error handler handles exception in reducer`() = runBlockingTest {
        testActionChannel.send(FulfillableInstantly)

        val expectedState = TestState(
            counter = INITIAL_COUNTER + 1,
            loading = false
        )

        assertEquals(expectedState, testStates.last())

        testActionChannel.send(TranslatesToExceptionInReducer)

        assertEquals(expectedState, testErrorHandler.lastState)
        assertTrue(testErrorHandler.lastThrowable is IllegalStateException)

        testActionChannel.send(FulfillableInstantly)

        val expectedStateAfterErrorHandling = expectedState.copy(counter = expectedState.counter + 1)

        assertEquals(expectedStateAfterErrorHandling, testStates.last())
    }

    @Test
    fun `event producer doesn't react on all effects`() = runBlockingTest {
        val actions = listOf(
            FulfillableInstantly,
            FulfillableInstantly,
            FulfillableInstantly
        )

        val events = mutableListOf<TestEvent>()

        val job = launch {
            baseStore.eventFlow
                .collect { events += it }
        }

        actions.forEach { testActionChannel.send(it) }

        job.cancel()

        assertTrue(events.isEmpty())
    }

    @Test
    fun `event producer reacts on special effect`() = runBlockingTest {
        val actions = listOf(
            FulfillableInstantly,
            ActionForEvent,
            FulfillableInstantly
        )

        val events = mutableListOf<TestEvent>()

        val job = launch {
            baseStore.eventFlow
                .collect { events += it }
        }

        actions.forEach { testActionChannel.send(it) }

        job.cancel()

        assertTrue(events.size == 1)
    }

    @Test
    fun `store doesn't save last event`() = runBlockingTest {
        val actions = listOf(
            FulfillableInstantly,
            ActionForEvent,
            FulfillableInstantly
        )

        actions.forEach { testActionChannel.send(it) }

        val events = mutableListOf<TestEvent>()
        val job = launch {
            baseStore.eventFlow
                .collect { events += it }
        }

        assertTrue(events.isEmpty())

        testActionChannel.send(ActionForEvent)

        job.cancel()

        assertTrue(events.size == 1)
    }
}
