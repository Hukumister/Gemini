package com.haroncode.gemini.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.haroncode.gemini.sample.di.DI
import com.haroncode.gemini.sample.util.setLaunchScreen
import javax.inject.Inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import toothpick.Toothpick

class AppActivity : AppCompatActivity() {

    private val navigator: Navigator = object : SupportAppNavigator(this, R.id.container) {
        override fun setupFragmentTransaction(
            command: Command,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction
        ) {
            fragmentTransaction.setReorderingAllowed(true)
        }
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)
        if (supportFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.Main)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            Toothpick.closeScope(DI.APP_SCOPE)
        }
    }
}
