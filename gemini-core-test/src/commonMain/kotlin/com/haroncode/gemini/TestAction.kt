package com.haroncode.gemini

sealed class TestAction {
    object Unfulfillable : TestAction()
    object FulfillableInstantly : TestAction()
    data class FulfillableAsync(val delayMs: Long) : TestAction()
    object TranslatesTo3Effects : TestAction()
    object MaybeFulfillable : TestAction()
    object ActionForEvent : TestAction()
    object LeadsToExceptionInMiddleware : TestAction()
    object TranslatesToExceptionInReducer : TestAction()
}
