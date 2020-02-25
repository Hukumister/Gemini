package com.haroncode.gemini.common

import com.haroncode.gemini.core.elements.Bootstrapper
import io.reactivex.processors.FlowableProcessor

class TestBootstrapper(
    private val testActionPublisher: FlowableProcessor<TestAction>
) : Bootstrapper<TestAction> {

    override fun invoke() = testActionPublisher.hide()
}
