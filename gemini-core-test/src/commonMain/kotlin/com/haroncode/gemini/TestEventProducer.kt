package com.haroncode.gemini

import com.haroncode.gemini.element.EventProducer

class TestEventProducer : EventProducer<TestState, TestEffect, TestEvent> {

    override fun invoke(state: TestState, effect: TestEffect): TestEvent? =
        if (effect is TestEffect.EffectForEvent) TestEvent.Simple
        else null
}
