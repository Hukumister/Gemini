package com.haroncode.gemini.android.binder.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.haroncode.gemini.core.StoreView
import com.haroncode.gemini.android.binder.viewmodel.ViewModelBinder
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber

abstract class MviFragment<Action : Any, State : Any> @JvmOverloads constructor(
    @LayoutRes contentLayoutId: Int = 0
) : Fragment(contentLayoutId), StoreView<Action, State> {

    private val source = PublishProcessor.create<Action>()

    abstract val viewModelBinder: ViewModelBinder<Action, *, State, *>

    override fun onStart() {
        super.onStart()
        viewModelBinder.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        viewModelBinder.unbindView()
    }

    abstract fun onViewStateChanged(state: State)

    fun postAction(action: Action) = source.onNext(action)

    final override fun accept(state: State) = onViewStateChanged(state)

    final override fun subscribe(subscriber: Subscriber<in Action>) = source.subscribe(source)

}