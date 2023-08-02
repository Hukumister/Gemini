package com.haroncode.gemini.sample

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.haroncode.gemini.sample.ui.AuthFragment
import com.haroncode.gemini.sample.ui.CounterFragment
import com.haroncode.gemini.sample.ui.MainFragment

object Screens {

    fun Main() = FragmentScreen {
        MainFragment()
    }

    fun Counter() = FragmentScreen {
        CounterFragment.newInstance()
    }

    fun Auth() = FragmentScreen {
        AuthFragment.newInstance()
    }
}
