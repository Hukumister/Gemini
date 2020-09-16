package com.haroncode.gemini

import com.haroncode.gemini.TestAction.ActionForEvent
import com.haroncode.gemini.TestAction.FulfillableAsync
import com.haroncode.gemini.TestAction.FulfillableInstantly
import com.haroncode.gemini.TestAction.LeadsToExceptionInMiddleware
import com.haroncode.gemini.TestAction.MaybeFulfillable
import com.haroncode.gemini.TestAction.TranslatesTo3Effects
import com.haroncode.gemini.TestAction.TranslatesToExceptionInReducer
import com.haroncode.gemini.TestAction.Unfulfillable
import com.haroncode.gemini.TestEffect.ConditionalThingHappened
import com.haroncode.gemini.TestEffect.EffectForEvent
import com.haroncode.gemini.TestEffect.FinishedAsync
import com.haroncode.gemini.TestEffect.InstantEffect
import com.haroncode.gemini.TestEffect.LeadsToExceptionInReducer
import com.haroncode.gemini.TestEffect.MultipleEffect1
import com.haroncode.gemini.TestEffect.MultipleEffect2
import com.haroncode.gemini.TestEffect.MultipleEffect3
import com.haroncode.gemini.TestEffect.StartedAsync
import com.haroncode.gemini.element.Middleware
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

const val DELAYED_FULFILL_AMOUNT = 5

const val CONDITIONAL_MULTIPLIER = 10

class TestMiddleware(
    private val asyncWorkDispatcher: CoroutineDispatcher
) : Middleware<TestAction, TestState, TestEffect> {

    override fun execute(action: TestAction, state: TestState): Flow<TestEffect> = when (action) {
        Unfulfillable -> emptyFlow()
        FulfillableInstantly -> flowOf(InstantEffect(1))
        ActionForEvent -> flowOf(EffectForEvent)
        is FulfillableAsync ->
            flowOf(DELAYED_FULFILL_AMOUNT)
                .map<Int, TestEffect> { amount ->
                    delay(action.delayMs)
                    FinishedAsync(amount)
                }
                .onStart { emit(StartedAsync) }
                .flowOn(asyncWorkDispatcher)
        TranslatesTo3Effects -> flowOf(
            MultipleEffect1,
            MultipleEffect2,
            MultipleEffect3
        )
        MaybeFulfillable ->
            if (state.counter % 3 == 0) flowOf(ConditionalThingHappened(CONDITIONAL_MULTIPLIER))
            else emptyFlow()
        LeadsToExceptionInMiddleware -> flow { throw Exception() }
        TranslatesToExceptionInReducer -> flowOf(LeadsToExceptionInReducer)
    }
}
