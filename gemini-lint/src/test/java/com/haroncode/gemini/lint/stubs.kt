package com.haroncode.gemini.lint

val BINDER_STUB = """
package com.haroncode.gemini.binder

import com.haroncode.gemini.binder.rule.*

object StoreViewBinding {
  fun <T : SavedStateRegistryOwner> with(
      lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
      factoryProvider: () -> BindingRulesFactory<T>,
  ): Binder<T> = SimpleBinder(
      factoryProvider = factoryProvider,
      lifecycleStrategy = lifecycleStrategy,
  )
  fun <T : SavedStateRegistryOwner> withRestore(
      lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
      factoryProvider: () -> BindingRulesFactory<T>,
  ): Binder<T> = RestoreBinder(
      factoryProvider = factoryProvider,
      lifecycleStrategy = lifecycleStrategy,
  )
  fun <T : LifecycleOwner> with(
      factory: BindingRulesFactory<T>,
      lifecycleStrategy: LifecycleStrategy = StartStopStrategy
  ): Binder<T> = BinderImpl(
      factory = factory,
      lifecycleStrategy = lifecycleStrategy
  )
}

interface Binder<View> {
    fun bind(view: View)
}

internal class BinderImpl<View : LifecycleOwner>(
    private val factory: BindingRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy
) : Binder<View> {
    override fun bind(view: View) { }
}
""".trimIndent()

val BINDER_RULES = """
package com.haroncode.gemini.binder.rule

interface BindingRulesFactory<P : Any> {
    fun create(param: P): Collection<BindingRule>
}

interface BindingRule {
    suspend fun bind()
}

fun <T : Any> generateSampleRulesFactory() = object : BindingRulesFactory<T> {
    override fun create(param: T): Collection<BindingRule> = emptyList()
}
""".trimIndent()

val OTHER_BINDER_STUB = """
package ru.test.app

object StoreViewBinding {
  fun <T : LifecycleOwner> with(
      factory: BindingRulesFactory<T>,
      lifecycleStrategy: LifecycleStrategy = StartStopStrategy
  ): Binder<T> = BinderImpl(
      factory = factory,
      lifecycleStrategy = lifecycleStrategy
  )
}

interface Binder<View> {
    fun bind(view: View)
}

class BinderImpl<View> : Binder<View> {
    override fun bind(view: View) {}
}
""".trimIndent()
