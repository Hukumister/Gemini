plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "${project.rootDir}/сodequality/ktlint.gradle.kts")

kotlin {
    android()
    ios()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.kotlinx.coroutines)
                api(project(":gemini-core"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.androidx.appCompat)
                implementation(Deps.androidx.lifecycleKtx)
            }
        }
    }
}

android {
    compileSdkVersion(Versions.android.compileSdk)
    buildToolsVersion(Versions.android.buildTools)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdkVersion(Versions.android.minSdk)
        targetSdkVersion(Versions.android.targetSdk)
    }
}
