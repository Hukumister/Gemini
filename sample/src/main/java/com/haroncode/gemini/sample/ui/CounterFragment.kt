package com.haroncode.gemini.sample.ui

import android.os.Bundle
import android.view.View
import com.haroncode.gemini.binder.StoreViewBinding
import com.haroncode.gemini.sample.R
import com.haroncode.gemini.sample.base.BaseFragment
import com.haroncode.gemini.sample.databinding.FragmentCounterBinding
import com.haroncode.gemini.sample.presentation.justreducer.CounterBindingFactory
import com.haroncode.gemini.sample.presentation.justreducer.CounterStore.Action
import javax.inject.Inject
import javax.inject.Provider

class CounterFragment : BaseFragment(R.layout.fragment_counter) {

    @Inject
    lateinit var factory: Provider<CounterBindingFactory>

    private var _binding: FragmentCounterBinding? = null
    private val binding get() = _binding!!

    private val storeViewDelegate = CounterStoreViewDelegate(this) { viewState ->
        binding.counterTextView.text = getString(R.string.fragment_counter_amount_format, viewState.count.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StoreViewBinding.withRestore(factoryProvider = factory::get)
            .bind(storeViewDelegate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCounterBinding.bind(view)
        binding.increaseButton.setOnClickListener { storeViewDelegate.emit(Action.Increment) }
        binding.decreaseButton.setOnClickListener { storeViewDelegate.emit(Action.Decrement) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = CounterFragment()
    }
}
