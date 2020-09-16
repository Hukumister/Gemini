package com.haroncode.gemini

sealed class TestEffect {
    data class InstantEffect(val amount: Int) : TestEffect()
    object StartedAsync : TestEffect()
    data class FinishedAsync(val amount: Int) : TestEffect()
    object MultipleEffect1 : TestEffect()
    object MultipleEffect2 : TestEffect()
    object MultipleEffect3 : TestEffect()
    data class ConditionalThingHappened(val multiplier: Int) : TestEffect()
    object EffectForEvent : TestEffect()
    object LeadsToExceptionInReducer : TestEffect()
}
