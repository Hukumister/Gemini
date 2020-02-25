package com.haroncode.gemini.core.elements

import org.reactivestreams.Publisher

/**
 * @author HaronCode.
 */
typealias Bootstrapper<Action> = () -> Publisher<Action>
