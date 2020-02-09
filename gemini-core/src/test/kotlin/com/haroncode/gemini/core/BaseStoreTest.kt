package com.haroncode.gemini.core

import com.haroncode.gemini.common.*
import com.haroncode.gemini.common.TestAction.*
import com.haroncode.gemini.connection.BaseConnectionRule
import com.haroncode.gemini.store.BaseStore
import io.reactivex.observers.TestObserver
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * @author HaronCode.
 */
class BaseStoreTest {

    private lateinit var baseStore: BaseStore<TestAction, TestState, TestViewEvent, TestEffect>
    private lateinit var asyncWorkScheduler: TestScheduler
    private lateinit var testBootstrapperSubject: PublishSubject<TestAction>

    private lateinit var testStoreView: TestStoreView
    private lateinit var testActionsSubject: PublishProcessor<TestAction>
    private lateinit var testStatesObserver: TestObserver<TestState>

    private lateinit var testBinder: TestConnectionBinder

    @Before
    fun prepare() {
        asyncWorkScheduler = TestScheduler()
        testActionsSubject = PublishProcessor.create()
        testBootstrapperSubject = PublishSubject.create()
        testStatesObserver = TestObserver()
        testBinder = TestConnectionBinder()

        testStoreView = TestStoreView(testActionsSubject, testStatesObserver)
        baseStore = BaseStore(
            initialState = TestState(),
            reducer = TestReducer(),
            bootstrapper = TestBootstrapper(testBootstrapperSubject),
            middleware = TestMiddleware(asyncWorkScheduler)
        )

        val storeToViewConnectionRule = BaseConnectionRule(
            consumer = testStoreView,
            publisher = baseStore,
            transformer = { input -> input }
        )

        val viewToStoreConnectionRule = BaseConnectionRule(
            consumer = baseStore,
            publisher = testStoreView,
            transformer = { input -> input }
        )

        testBinder.bind(storeToViewConnectionRule)
        testBinder.bind(viewToStoreConnectionRule)

        testBinder.connectAll()
    }

    @Test
    fun `if there are no actions, feature only emits initial state`() {
        assertEquals(1, testStatesObserver.onNextEvents().size)
    }

    @Test
    fun `emitted initial state is correct`() {
        val state = testStatesObserver.onNextEvents().first()
        assertEquals(INITIAL_COUNTER, state.counter)
        assertEquals(INITIAL_LOADING, state.loading)
    }

    @Test
    fun `bootstraper correct work after create store`() {
        val actions = listOf(
            Unfulfillable,
            Unfulfillable,
            Unfulfillable
        )

        actions.forEach(testBootstrapperSubject::onNext)

        assertEquals(1, testStatesObserver.onNextEvents().size)
    }

    @Test
    fun `bootstraper correct work after create store2`() {
        val actions = listOf(
            FulfillableInstantly,
            FulfillableInstantly,
            FulfillableInstantly
        )

        actions.forEach(testBootstrapperSubject::onNext)

        assertEquals(1 + actions.size, testStatesObserver.onNextEvents().size)
    }

    @Test
    fun `there should be no state emission besides the initial one for unfulfillable wishes`() {
        val actions = listOf(
            Unfulfillable,
            Unfulfillable,
            Unfulfillable
        )

        actions.forEach(testActionsSubject::onNext)

        assertEquals(1, testStatesObserver.onNextEvents().size)
    }

    @Test
    fun `there should be the same amount of states as actions that translate 1 - 1 to effects plus one for initial state`() {
        val actions = listOf(
            FulfillableInstantly,
            FulfillableInstantly,
            FulfillableInstantly
        )

        actions.forEach(testActionsSubject::onNext)

        assertEquals(1 + actions.size, testStatesObserver.onNextEvents().size)
    }

    @Test
    fun `there should be 3 times as many states as actions that translate 1 - 3 to effects plus one for initial state`() {
        val actions = listOf(
            TranslatesTo3Effects,
            TranslatesTo3Effects,
            TranslatesTo3Effects
        )

        actions.forEach(testActionsSubject::onNext)

        assertEquals(1 + actions.size * 3, testStatesObserver.onNextEvents().size)
    }

    @Test
    fun `the number of state emissions should reflect the number of effects plus one for initial state in complex case`() {
        val actions = listOf(
            FulfillableInstantly,  // maps to 1 effect
            FulfillableInstantly,  // maps to 1 effect
            MaybeFulfillable,       // maps to 1 in this case
            Unfulfillable,          // maps to 0
            FulfillableInstantly,  // maps to 1
            FulfillableInstantly,  // maps to 1
            MaybeFulfillable,       // maps to 0 in this case
            TranslatesTo3Effects    // maps to 3
        )

        actions.forEach(testActionsSubject::onNext)

        assertEquals(8 + 1, testStatesObserver.onNextEvents().size)
        val expectedState = TestState(
            counter = (INITIAL_COUNTER + 1 + 1) * 10 + 1 + 1 + 3 * 1,
            loading = false
        )
        assertEquals(expectedState, testStatesObserver.onNextEvents().last())
    }


    @Test
    fun `there should be no state emission after store destroying`() {
        val mockServerDelayMs = 10L

        testActionsSubject.onNext(FulfillableAsync(mockServerDelayMs))

        assertEquals(2, testStatesObserver.onNextEvents().size)

        val stateBeforeDestroy = testStatesObserver.onNextEvents().last()

        assertEquals(INITIAL_COUNTER, stateBeforeDestroy.counter)
        assertEquals(true, stateBeforeDestroy.loading)

        asyncWorkScheduler.advanceTimeBy(5, TimeUnit.MILLISECONDS)

        testBinder.dispose()
        baseStore.dispose()

        asyncWorkScheduler.advanceTimeBy(10, TimeUnit.MILLISECONDS)

        val stateAfterDestroy = testStatesObserver.onNextEvents().last()

        assertEquals(INITIAL_COUNTER, stateAfterDestroy.counter)
        assertEquals(true, stateAfterDestroy.loading)
    }

    @Test
    fun `last state should be delivered after view rebind`() {
        val mockServerDelayMs = 10L

        testActionsSubject.onNext(FulfillableAsync(mockServerDelayMs))

        assertEquals(2, testStatesObserver.onNextEvents().size)

        val stateBeforeUnbind = testStatesObserver.onNextEvents().last()

        assertEquals(INITIAL_COUNTER, stateBeforeUnbind.counter)
        assertEquals(true, stateBeforeUnbind.loading)

        asyncWorkScheduler.advanceTimeBy(5, TimeUnit.MILLISECONDS)

        testBinder.disconnectAll()

        asyncWorkScheduler.advanceTimeBy(10, TimeUnit.MILLISECONDS)

        testBinder.connectAll()

        val stateAfterRebind = testStatesObserver.onNextEvents().last()

        assertEquals(INITIAL_COUNTER + DELAYED_FULFILL_AMOUNT, stateAfterRebind.counter)
        assertEquals(false, stateAfterRebind.loading)
    }
}