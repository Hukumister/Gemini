package com.haroncode.gemini

import com.haroncode.gemini.element.Bootstrapper
import kotlinx.coroutines.flow.Flow

class TestBootstrapper(
    private val testActionFlow: Flow<TestAction>
) : Bootstrapper<TestAction> {

    override fun bootstrap() = testActionFlow
}
