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

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("debug") {
                from(components["debug"])
            }

            create<MavenPublication>("release") {
                from(components["release"])
            }
        }
    }
}

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

dependencies {
    implementation(Deps.kotlinx.coroutines)
    api(project(":gemini-core"))
}
