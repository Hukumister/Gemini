package com.haroncode.gemini.sample.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.haroncode.gemini.android.StoreViewConnector
import com.haroncode.gemini.core.StoreEventListener
import com.haroncode.gemini.sample.R
import com.haroncode.gemini.sample.base.PublisherFragment
import com.haroncode.gemini.sample.databinding.FragmentAuthBinding
import com.haroncode.gemini.sample.presentation.onlyaction.AuthConnectionFactory
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.Action
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.Action.LoginClick
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.State
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.BackpressureStrategy
import javax.inject.Inject

class AuthFragment : PublisherFragment<Action, State>(R.layout.fragment_auth),
    StoreEventListener<AuthStore.Event> {

    @Inject
    lateinit var factory: AuthConnectionFactory

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        StoreViewConnector.withFactory(factory)
            .connect(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAuthBinding.bind(view)
        binding.loginButton.setOnClickListener { postAction(LoginClick) }
    }

    override fun onStart() {
        super.onStart()

        binding.emailEditText.textChanges()
            .skipInitialValue()
            .toFlowable(BackpressureStrategy.LATEST)
            .map(CharSequence::toString)
            .map(Action::ChangeEmail)
            .subscribe(actionProcessor)

        binding.passwordEdit.textChanges()
            .skipInitialValue()
            .toFlowable(BackpressureStrategy.LATEST)
            .map(CharSequence::toString)
            .map(Action::ChangePassword)
            .subscribe(actionProcessor)
    }

    override fun onViewStateChanged(viewState: State) {
        binding.progressBar.isVisible = viewState.isLoading
        binding.loginButton.isEnabled = viewState.isButtonLoginEnable

        binding.emailLayout.error = viewState.emailErrorHint
        binding.passwordLayout.error = viewState.passwordErrorHint
    }

    companion object {
        fun newInstance() = CounterFragment()
    }

    override fun onEvent(event: AuthStore.Event) = when (event) {
        is AuthStore.Event.Success -> Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
        is AuthStore.Event.Fail -> Toast.makeText(requireContext(), "Fail", Toast.LENGTH_SHORT).show()
    }
}
