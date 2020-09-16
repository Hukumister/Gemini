package com.haroncode.gemini.sample.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.haroncode.gemini.StoreEventListener
import com.haroncode.gemini.binder.StoreViewBinding
import com.haroncode.gemini.sample.R
import com.haroncode.gemini.sample.base.PublisherFragment
import com.haroncode.gemini.sample.databinding.FragmentAuthBinding
import com.haroncode.gemini.sample.presentation.onlyaction.AuthBindingFactory
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.Action
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.Action.LoginClick
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.State
import javax.inject.Inject
import javax.inject.Provider

class AuthFragment :
    PublisherFragment<Action, State>(R.layout.fragment_auth),
    StoreEventListener<AuthStore.Event> {

    @Inject
    lateinit var factory: Provider<AuthBindingFactory>

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StoreViewBinding.withRestore(factoryProvider = factory::get)
            .bind(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAuthBinding.bind(view)
        binding.loginButton.setOnClickListener { postAction(LoginClick) }

        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            postAction(Action.ChangeEmail(text.toString()))
        }

        binding.passwordEdit.doOnTextChanged { text, _, _, _ ->
            postAction(Action.ChangePassword(text.toString()))
        }
    }

    override fun onViewStateChanged(viewState: State) {
        binding.progressBar.isVisible = viewState.isLoading
        binding.loginButton.isEnabled = viewState.isButtonLoginEnable

        binding.emailLayout.error = viewState.emailErrorHint
        binding.passwordLayout.error = viewState.passwordErrorHint
    }

    companion object {
        fun newInstance() = AuthFragment()
    }

    override fun onEvent(event: AuthStore.Event) = when (event) {
        is AuthStore.Event.Success -> Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
        is AuthStore.Event.Fail -> Toast.makeText(requireContext(), "Fail", Toast.LENGTH_SHORT).show()
    }
}
