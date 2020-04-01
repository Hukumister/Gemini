package com.haroncode.gemini.sample.ui

import android.os.Bundle
import android.view.View
import com.haroncode.gemini.android.StoreViewConnector
import com.haroncode.gemini.sample.R
import com.haroncode.gemini.sample.base.PublisherFragment
import com.haroncode.gemini.sample.databinding.FragmentMainBinding
import com.haroncode.gemini.sample.presentation.routing.MainConnectionFactory
import com.haroncode.gemini.sample.presentation.routing.MainStore
import javax.inject.Inject

class MainFragment : PublisherFragment<MainStore.Action, Unit>(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: MainConnectionFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StoreViewConnector.withFactory(factory).connect(this)
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
