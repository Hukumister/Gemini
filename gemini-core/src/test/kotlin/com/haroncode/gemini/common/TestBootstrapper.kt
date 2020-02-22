package com.haroncode.gemini.common

import com.haroncode.gemini.core.elements.Bootstrapper
import io.reactivex.Observable
import io.reactivex.subjects.Subject

class TestBootstrapper(
    private val testActionSubject: Subject<TestAction>
) : Bootstrapper<TestAction> {

    override fun invoke(): Observable<TestAction> = testActionSubject.hide()
}
