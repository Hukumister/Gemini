package com.haroncode.gemini.sample.ui

import android.os.Bundle
import android.view.View
import com.haroncode.gemini.android.StoreViewConnector
import com.haroncode.gemini.sample.R
import com.haroncode.gemini.sample.base.PublisherFragment
import com.haroncode.gemini.sample.presentation.justreducer.CounterConnectionFactory
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore.Action
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore.State
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_counter.*

class CounterFragment : PublisherFragment<Action, State>(R.layout.fragment_counter) {

    @Inject
    lateinit var counterFactory: CounterConnectionFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StoreViewConnector.withFactory(counterFactory)
            .connect(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        increaseButton.setOnClickListener { postAction(Action.Increment) }
    }

    override fun onViewStateChanged(viewState: State) {
        counterTextView.text = getString(R.string.fragment_counter_amount_format, viewState.count.toString())
    }

    companion object {
        fun newInstance() = CounterFragment()
    }
}
