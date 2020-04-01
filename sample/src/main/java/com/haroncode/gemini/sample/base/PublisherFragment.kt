package com.haroncode.gemini.sample.base

import androidx.annotation.LayoutRes
import com.haroncode.gemini.core.StoreView
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber

abstract class PublisherFragment<Action : Any, ViewState : Any> @JvmOverloads constructor(
    @LayoutRes layoutRes: Int = 0
) : BaseFragment(layoutRes), StoreView<Action, ViewState> {

    protected val actionProcessor = PublishProcessor.create<Action>()

    fun postAction(action: Action) = actionProcessor.onNext(action)

    abstract fun onViewStateChanged(viewState: ViewState)

    final override fun accept(viewState: ViewState) = onViewStateChanged(viewState)

    override fun subscribe(subscriber: Subscriber<in Action>) = actionProcessor.subscribe(subscriber)
}
