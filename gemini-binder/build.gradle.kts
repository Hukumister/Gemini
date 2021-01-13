plugins {
    id("ktlint-plugin")
    id("publish-plugin")
    kotlin("multiplatform")
    id("com.android.library")
}

val geminiGroup = findProperty("group") as String
val geminiVersion = findProperty("version") as String

version = geminiVersion
group = geminiGroup

kotlin {
    android { publishLibraryVariants("release", "debug") }
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
                implementation(Deps.androidx.viewModel)
                api(project(":gemini-store-keeper"))
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
