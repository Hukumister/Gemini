package com.haroncode.gemini.sample

import com.haroncode.gemini.sample.ui.CounterFragment
import com.haroncode.gemini.sample.ui.MainFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object Main : SupportAppScreen() {
        override fun getFragment() = MainFragment()
    }

    object Counter : SupportAppScreen() {
        override fun getFragment() = CounterFragment.newInstance()
    }
}
