package com.haroncode.gemini.sample.domain.system

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

interface InternetStatusObserver {

    fun observe(): Flowable<Boolean>
}

class InternetStatusObserverImpl @Inject constructor(
    private val context: Context
) : InternetStatusObserver {


    init {


    }

    private val processor = BehaviorProcessor.create<Boolean>()

    override fun observe(): Flowable<Boolean> = processor.hide()
}