object Deps {
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.android.gradlePlugin}"

    const val bintrayGradlePlugin = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}"

    object kotlinx {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx.coroutines}"
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinx.coroutines}"
    }

    const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"

    object androidx {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.androidx.appCompat}"
        const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidx.lifecycle}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:${Versions.androidx.lifecycle}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.androidx.core}"
        const val material = "com.google.android.material:material:${Versions.androidx.material}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidx.constraintLayout}"
    }

    object toothpick {
        const val ktp = "com.github.stephanenicolas.toothpick:ktp:${Versions.toothpick}"
        const val compiler = "com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothpick}"
    }

    object lint {
        const val core = "com.android.tools.lint:lint:${Versions.androidTools}"
        const val api = "com.android.tools.lint:lint-api:${Versions.androidTools}"
        const val checks = "com.android.tools.lint:lint-checks:${Versions.androidTools}"
        const val tests = "com.android.tools.lint:lint-tests:${Versions.androidTools}"
    }
    const val testutils = "com.android.tools:testutils:${Versions.androidTools}"

    const val cicerone = "ru.terrakok.cicerone:cicerone:${Versions.cicerone}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"
}
