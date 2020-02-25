package com.haroncode.gemini.common

import com.haroncode.gemini.core.elements.EventProducer

class TestEventProducer : EventProducer<TestState, TestEffect, TestViewEvent> {

    override fun invoke(state: TestState, effect: TestEffect): TestViewEvent? {
        return if (effect is TestEffect.EffectForEvent) {
            TestViewEvent.SimpleEvent
        } else {
            null
        }
    }
}
