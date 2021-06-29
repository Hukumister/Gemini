package com.haroncode.gemini.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest

@Suppress("UnstableApiUsage")
class WrongGenimiUsageDetectorTest : LintDetectorTest() {
    companion object {
        private val GEMINI_STUB1 = kotlin(BINDER_STUB)
        private val GEMINI_STUB2 = kotlin(BINDER_RULES)
        private val OTHER_STUB = kotlin(OTHER_BINDER_STUB)
    }

    fun testRightUsageWithActivity() {
        lint()
            .files(
                GEMINI_STUB1,
                GEMINI_STUB2,
                kotlin("""
                    package ru.test.app
                    
                    import android.app.Activity
                    import com.haroncode.gemini.binder.*
                    import com.haroncode.gemini.binder.rule.*
                    
                    class Activity1 : Activity() {
                         override fun onCreate(savedInstanceState: Bundle?) {
                            super.onCreate(savedInstanceState)
                    
                            StoreViewBinding.with(generateSampleRulesFactory<Any>())
                                .bind(this)
                        }
                    }
                """.trimIndent())
            )
            .requireCompileSdk()
            .run()
            .expectClean()
    }

    fun testRightUsageWithFragment() {
        lint()
            .files(
                GEMINI_STUB1,
                GEMINI_STUB2,
                kotlin("""
                    package ru.test.app
                    
                    import android.app.Fragment
                    import com.haroncode.gemini.binder.*
                    import com.haroncode.gemini.binder.rule.*
                    
                    class Fragment1 : Fragment() {
                         override fun onCreate(savedInstanceState: Bundle?) {
                            super.onCreate(savedInstanceState)
                    
                            StoreViewBinding.with(generateSampleRulesFactory<Any>())
                                .bind(this)
                        }
                    }
                """.trimIndent())
            )
            .requireCompileSdk()
            .run()
            .expectClean()
    }

    fun testWrongUsageWithFragment_onViewCreated() {
        lint()
            .files(
                GEMINI_STUB1,
                GEMINI_STUB2,
                kotlin("""
                    package ru.test.app
                    
                    import android.app.Fragment
                    import com.haroncode.gemini.binder.*
                    import com.haroncode.gemini.binder.rule.*
                    
                    class Fragment1 : Fragment() {
                        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                            super.onViewCreated(view, savedInstanceState)

                            StoreViewBinding.with(generateSampleRulesFactory<Any>())
                                .bind(this)
                        }
                    }
                """.trimIndent())
            )
            .requireCompileSdk()
            .run()
            .expect("""
                src/ru/test/app/Fragment1.kt:11: Error: Calling bind from onViewCreated instead of onCreate [ShouldBeCalledInOnCreate]
                        StoreViewBinding.with(generateSampleRulesFactory<Any>())
                        ^
                1 errors, 0 warnings
            """.trimIndent())
    }

    fun testWrongUsageWithActivity_onResume() {
        lint()
            .files(
                GEMINI_STUB1,
                GEMINI_STUB2,
                kotlin("""
                    package ru.test.app
                    
                    import android.app.Activity
                    import com.haroncode.gemini.binder.*
                    import com.haroncode.gemini.binder.rule.*
                    
                    class Activity1 : Activity() {
                        override fun onResume() {
                            super.onViewCreated(view, savedInstanceState)

                            StoreViewBinding.with(generateSampleRulesFactory<Any>())
                                .bind(this)
                        }
                    }
                """.trimIndent())
            )
            .requireCompileSdk()
            .run()
            .expect("""
                src/ru/test/app/Activity1.kt:11: Error: Calling bind from onResume instead of onCreate [ShouldBeCalledInOnCreate]
                        StoreViewBinding.with(generateSampleRulesFactory<Any>())
                        ^
                1 errors, 0 warnings
            """.trimIndent())
    }

    fun testDoesNotTriggeredByOtherClasses() {
        lint()
            .files(
                OTHER_STUB,
                kotlin("""
                    package ru.test.app
                    
                    import android.app.Fragment
                    import ru.test.lib.*
                    
                    class Fragment1 : Fragment() {
                        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                            super.onViewCreated(view, savedInstanceState)

                            StoreViewBinding.with().bind(this)
                        }
                    }
                """.trimIndent())
            )
            .requireCompileSdk()
            .run()
            .expectClean()

    }

    override fun getDetector() = WrongGenimiUsageDetector()

    override fun getIssues() = WrongGenimiUsageDetector.issues
}
