package com.haroncode.gemini.sample

import com.haroncode.gemini.sample.ui.CounterFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object Main : SupportAppScreen()

    object Counter : SupportAppScreen() {
        override fun getFragment() = CounterFragment.newInstance()
    }
}
