package com.haroncode.gemini.android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.haroncode.gemini.binder.dsl.noneTransformer
import com.haroncode.gemini.binder.dsl.scheduleTransformer
import com.haroncode.gemini.binder.dsl.with
import com.haroncode.gemini.core.Binder
import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.StoreView
import com.haroncode.gemini.android.binder.AndroidBinder
import com.haroncode.gemini.android.binder.ViewBinding
import com.haroncode.gemini.android.binder.strategies.ResumePauseStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.reactivestreams.Subscriber

class TestFragment : Fragment(), StoreView<String, String> {

    private lateinit var testBinding: TestBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AndroidBinder
            .withBinding(testBinding)
            .withStrategy(ResumePauseStrategy)
            .create(this)
            .addTo(compositeDisposable)
    }

    class TestBinding(
        private val store: Store<String, String, String>
    ) : ViewBinding<TestFragment> {

        override fun onCreate(view: TestFragment, binder: Binder) {
            binder.bind(view to store with noneTransformer())
            binder.bind(store to view with scheduleTransformer(AndroidSchedulers.mainThread()))
        }
    }

    override fun accept(t: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe(s: Subscriber<in String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}