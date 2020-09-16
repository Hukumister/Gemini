package com.haroncode.gemini

import com.haroncode.gemini.TestEffect.ConditionalThingHappened
import com.haroncode.gemini.TestEffect.EffectForEvent
import com.haroncode.gemini.TestEffect.FinishedAsync
import com.haroncode.gemini.TestEffect.InstantEffect
import com.haroncode.gemini.TestEffect.LeadsToExceptionInReducer
import com.haroncode.gemini.TestEffect.MultipleEffect1
import com.haroncode.gemini.TestEffect.MultipleEffect2
import com.haroncode.gemini.TestEffect.MultipleEffect3
import com.haroncode.gemini.TestEffect.StartedAsync
import com.haroncode.gemini.element.Reducer

class TestReducer : Reducer<TestState, TestEffect> {

    override fun reduce(state: TestState, effect: TestEffect): TestState = when (effect) {
        is InstantEffect -> state.copy(counter = state.counter + effect.amount)
        is FinishedAsync -> state.copy(counter = state.counter + effect.amount, loading = false)
        is ConditionalThingHappened -> state.copy(counter = state.counter * effect.multiplier)
        StartedAsync -> state.copy(loading = true)
        EffectForEvent -> state.copy()
        MultipleEffect1,
        MultipleEffect2,
        MultipleEffect3 -> state.copy(counter = state.counter + 1)
        LeadsToExceptionInReducer -> throw IllegalStateException()
    }
}
