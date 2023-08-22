plugins {
    id("com.android.library")
    kotlin("android")
    id("convention.publication")
}

android {
    namespace = "com.haroncode.gemini.keeper"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.coroutines)
    implementation(libs.lifecycle.viewmodel)
    api(project(":gemini-core"))
}

val geminiGroup = findProperty("group") as String
val geminiVersion = findProperty("version") as String

version = geminiVersion
group = geminiGroup
