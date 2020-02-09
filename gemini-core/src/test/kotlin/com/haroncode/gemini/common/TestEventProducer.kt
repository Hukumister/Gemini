package com.haroncode.gemini.common

import com.haroncode.gemini.core.elements.EventProducer

class TestEventProducer : EventProducer<TestState, TestEffect, TestViewEvent> {

    override fun invoke(state: TestState, effect: TestEffect): TestViewEvent? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}