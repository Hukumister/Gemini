package com.haroncode.gemini

expect open class StoreViewDelegate<Action : Any, State : Any> : StoreView<Action, State> {

    fun emit(action: Action)
}
