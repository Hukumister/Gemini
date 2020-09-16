plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

repositories {
    maven {
        setUrl("https://dl.bintray.com/haroncode/maven")
    }
}

val geminiVersion = findProperty("version") as String

android {
    compileSdkVersion(Versions.android.compileSdk)
    buildToolsVersion(Versions.android.buildTools)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdkVersion(Versions.android.minSdk)
        targetSdkVersion(Versions.android.targetSdk)
    }
}

kotlin {
    jvm()
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.haroncode.gemini:gemini-core:$geminiVersion")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("com.haroncode.gemini:gemini-binder:$geminiVersion")
            }
        }
    }
}
