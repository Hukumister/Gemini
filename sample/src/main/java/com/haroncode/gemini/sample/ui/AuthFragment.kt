package com.haroncode.gemini.sample.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.haroncode.gemini.android.StoreViewConnector
import com.haroncode.gemini.core.EventListener
import com.haroncode.gemini.sample.base.PublisherFragment
import com.haroncode.gemini.sample.presentation.onlyaction.AuthConnectionFactory
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore
import javax.inject.Inject


class AuthFragment : PublisherFragment<AuthStore.Action, AuthStore.State>(),
    EventListener<AuthStore.Event> {

    @Inject
    lateinit var factory: AuthConnectionFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StoreViewConnector.withFactory(factory)
            .connect(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onViewStateChanged(viewState: AuthStore.State) {

    }

    companion object {
        fun newInstance() = CounterFragment()
    }

    override fun onEvent(event: AuthStore.Event) = when (event) {
        is AuthStore.Event.Success -> Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
        is AuthStore.Event.Fail -> Toast.makeText(requireContext(), "Fail", Toast.LENGTH_SHORT).show()
    }
}
