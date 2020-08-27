package com.haroncode.gemini.sample.presentation.routing

import android.os.Handler
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen

class HandlerRouter(
    private val handler: Handler
) : Router() {

    override fun newChain(vararg screens: Screen) {
        handler.post { super.newChain(*screens) }
    }

    override fun exit() {
        handler.post { super.exit() }
    }

    override fun navigateTo(screen: Screen) {
        handler.post { super.navigateTo(screen) }
    }

    override fun finishChain() {
        handler.post { super.finishChain() }
    }

    override fun backTo(screen: Screen?) {
        handler.post { super.backTo(screen) }
    }

    override fun newRootChain(vararg screens: Screen) {
        handler.post { super.newRootChain(*screens) }
    }

    override fun replaceScreen(screen: Screen) {
        handler.post { super.replaceScreen(screen) }
    }

    override fun newRootScreen(screen: Screen) {
        handler.post { super.newRootScreen(screen) }
    }
}
