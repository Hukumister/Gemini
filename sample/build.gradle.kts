plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

apply(from = "${project.rootDir}/сodequality/ktlint.gradle")

android {
    compileSdkVersion(Versions.android.compileSdk)
    buildToolsVersion(Versions.android.buildTools)

    defaultConfig {
        applicationId = "com.haroncode.gemini.sample"
        minSdkVersion(Versions.android.minSdk)
        targetSdkVersion(Versions.android.targetSdk)
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation(Deps.kotlinx.coroutines)

    implementation(Deps.androidx.appCompat)
    implementation(Deps.androidx.lifecycleKtx)
    implementation(Deps.androidx.coreKtx)
    implementation(Deps.androidx.material)
    implementation(Deps.androidx.constraintLayout)

    implementation(Deps.toothpick.ktp)
    kapt(Deps.toothpick.compiler)

    implementation(Deps.cicerone)

    implementation(Deps.timber)

    implementation(project(":gemini-android"))
}
