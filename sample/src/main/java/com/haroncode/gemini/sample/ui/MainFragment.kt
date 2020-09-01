package com.haroncode.gemini.sample.ui

import android.os.Bundle
import android.view.View
import com.haroncode.gemini.android.binder.StoreViewBinding
import com.haroncode.gemini.sample.R
import com.haroncode.gemini.sample.base.PublisherFragment
import com.haroncode.gemini.sample.databinding.FragmentMainBinding
import com.haroncode.gemini.sample.presentation.routing.MainBindingFactory
import com.haroncode.gemini.sample.presentation.routing.MainStore
import javax.inject.Inject
import javax.inject.Provider

class MainFragment : PublisherFragment<MainStore.Action, Unit>(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: Provider<MainBindingFactory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StoreViewBinding.withRestore(factoryProvider = factory::get)
            .bind(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        binding.counterBtn.setOnClickListener { postAction(MainStore.Action.COUNTER) }
        binding.authBtn.setOnClickListener { postAction(MainStore.Action.AUTH) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewStateChanged(viewState: Unit) = Unit
}
