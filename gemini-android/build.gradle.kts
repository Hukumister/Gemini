plugins {
    id("com.android.library")
    kotlin("android")
}

apply(from = "${project.rootDir}/сodequality/ktlint.gradle.kts")

android {
    compileSdkVersion(Versions.android.compileSdk)
    buildToolsVersion(Versions.android.buildTools)

    defaultConfig {
        minSdkVersion(Versions.android.minSdk)
        targetSdkVersion(Versions.android.targetSdk)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(Deps.kotlinx.coroutines)

    implementation(Deps.androidx.appCompat)
    implementation(Deps.androidx.lifecycleKtx)

    api(project(":gemini-binder"))
}